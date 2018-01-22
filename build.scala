#!/bin/sh
exec scala "$0" "$@"
!#
import java.io.File

import scala.sys.process._

abstract class BaseProjectDef(val name: String,
                              val localPath: String,
                              val gitBaseUrl: String = "git@github.com:ONSdigital",
                              val branch: Option[String],
                              val buildCommand:  String)

abstract class CategorisedProjectDef(val category: String,
                                     override val name: String,
                                     override val buildCommand: String,
                                     override val branch: Option[String])
  extends BaseProjectDef(name = name,
    localPath = s"projects/$category/$name",
    buildCommand = buildCommand,
    branch = branch
  )

case class PluginProject(override val name: String, override val branch: Option[String] = None)
  extends CategorisedProjectDef(category = "plugin", name = name, buildCommand = "sbt clean compile publishLocal", branch = branch)

case class ApiProject(override val name: String, override val branch: Option[String] = None)
  extends CategorisedProjectDef(category = "api", name = name, buildCommand = "sbt clean compile docker:publishLocal", branch = branch)

case class UiProject(override val name: String, override val branch: Option[String] = None)
  extends CategorisedProjectDef(category = "ui", name = name, buildCommand = "docker build -t sbr-ui .", branch = branch)

object Projects {

  private val definitions = List(
    PluginProject("sbt-code-quality"),
    PluginProject("sbt-scala-defaults"),
    PluginProject("sbt-play-defaults"),
    ApiProject("sbr-api", Some("feature/REG-362")),
    ApiProject("sbr-control-api", Some("feature/REG-353")),
    ApiProject("sbr-admin-data", Some("feature/REG-353")),
    UiProject("sbr-ui", Some("feature/dockerfile"))
  )

  def build() {
    println(io.Source.fromFile("banner.txt").mkString)
    gitUpdate()
    buildProjects()
  }

  private def gitUpdate() {
    println("Populating projects from git repos...")
    definitions.foreach(d => {
      val gitUrl = s"${d.gitBaseUrl}/${d.name}.git"
      if (!new File(d.localPath).exists()) {
        println(s"Cloning ${gitUrl}...")
        (s"git clone ${gitUrl} ${d.localPath}" !)
      }
      if (d.branch.isDefined) {
        println(s"Checking out branch ${d.branch.get} on repo ${gitUrl}...")
        Process(s"git checkout ${d.branch.get}", new File(d.localPath)).!
      }
      s"cd ${d.localPath} && git pull" !
    })
  }

  private def buildProjects() {
    definitions.foreach(d => {
      println(s"Building project ${d.name}...")
      Process(d.buildCommand, new File(d.localPath)).!
    })
  }

}

Projects.build()
