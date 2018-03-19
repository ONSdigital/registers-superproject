#!/bin/sh
exec scala "$0" "$@"
!#
import java.io.File

import scala.sys.process._

abstract class ProjectDef(val name: String,
                          val category: String,
                          val branch: Option[String],
                          val buildCommand:  String) {
  val localPath = s"projects/$category/$name"
  val gitBaseUrl: String = "https://github.com/ONSdigital"
}

case class PluginProject(override val name: String, override val branch: Option[String] = None)
  extends ProjectDef(name = name, category = "plugin", buildCommand = "sbt clean compile publishLocal", branch = branch)

case class ApiProject(override val name: String, override val branch: Option[String] = None)
  extends ProjectDef(name = name, category = "api", buildCommand = "sbt clean compile docker:publishLocal", branch = branch)

case class UiProject(override val name: String, override val branch: Option[String] = None)
  extends ProjectDef(name = name, category = "ui", buildCommand = "docker build -t sbr-ui .", branch = branch)

object Projects {

  private val definitions = List(
    PluginProject("sbt-code-quality"),
    PluginProject("sbt-scala-defaults"),
    PluginProject("sbt-play-defaults"),
    ApiProject("sbr-api", Some("develop")),
    ApiProject("sbr-control-api", Some("develop")),
    ApiProject("sbr-admin-data", Some("develop")),
    UiProject("sbr-ui", Some("develop"))
  )

  def build(args: Array[String]) {
    println(io.Source.fromFile("banner.txt").mkString)
    val filteredDefinitions = definitions.filter(project => args.isEmpty || args.exists(_.equals(project.name)))
    println(filteredDefinitions.map(_.name).mkString("Building projects ", ",", "..."))
    gitUpdate(filteredDefinitions, args)
    buildProjects(filteredDefinitions)
  }

  private def gitUpdate(definitions: List[ProjectDef], args: Array[String]) {
    println("Populating projects from git repos...")
    definitions.foreach(d => {
      val gitUrl = s"${d.gitBaseUrl}/${d.name}.git"
      if (!new File(d.localPath).exists()) {
        println(s"Cloning $gitUrl...")
        s"git clone $gitUrl ${d.localPath}" !
      }
      if (d.branch.isDefined) {
        println(s"Checking out branch ${d.branch.get} on repo $gitUrl...")
        Process(s"git checkout ${d.branch.get}", new File(d.localPath)).!
      }
      Process("git pull", new File(d.localPath)).!
    })
  }

  private def buildProjects(definitions: List[ProjectDef]) {
    definitions.foreach(d => {
      println(s"Building project ${d.name}...")
      Process(d.buildCommand, new File(d.localPath)).!
    })
  }

}

Projects.build(args)
