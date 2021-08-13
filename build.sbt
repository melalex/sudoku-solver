name := "sudoku-solver"

version := "0.1"

scalaVersion := "2.13.6"

idePackagePrefix := Some("com.melalex.sudoku")

libraryDependencies ++= {
  val catsVersion = "2.6.1"

  List("org.typelevel" %% "cats-core" % catsVersion)
}

