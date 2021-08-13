package com.melalex.sudoku

import scala.annotation.tailrec

object SudokuSolverApp extends App {

  type Sudoku = Vector[Vector[Byte]]
  type Pos    = (Int, Int)

  val Height      = 9
  val Width       = 9
  val BoxHeight   = 9
  val BoxWidth    = 9
  val ValidValues = (1 to 9).map(_.toByte).view

  def findNextEmptyCell(sudoku: Sudoku): Option[Pos] = {

    @tailrec
    def find(i: Int, j: Int): Option[Pos] =
      if (sudoku(i)(j) == 0) Some((i, j))
      else if (i >= Height) None
      else if (j >= Width) find(i + 1, 0)
      else find(i, j + 1)

    find(0, 0)
  }

  def canFillCellWith(sudoku: Sudoku, pos: Pos, value: Byte): Boolean = {
    def containsInBox(i: Int, j: Int): Boolean = {
      val boxRowZero    = (i / BoxHeight) * BoxHeight
      val boxColumnZero = (j / BoxWidth) * BoxWidth

      for {
        boxI <- 0 until BoxHeight
        boxJ <- 0 until BoxWidth if sudoku(boxRowZero + boxI)(boxColumnZero + boxJ) == value
      } return true

      false
    }

    val (i, j)           = pos
    val containsInRow    = sudoku(i).contains(value)
    val containsInColumn = sudoku.indices.exists(i => sudoku(i)(j) == value)

    !(containsInRow || containsInColumn || containsInBox(i, j))
  }

  def fillCell(sudoku: Sudoku, pos: Pos, value: Byte): Sudoku = {
    val (i, j) = pos

    sudoku.updated(i, sudoku(i).updated(j, value))
  }

  def solve(sudoku: Sudoku): Option[Sudoku] =
    findNextEmptyCell(sudoku) match {
      case None => Some(sudoku)
      case Some(nextPos) =>
        ValidValues
          .withFilter(canFillCellWith(sudoku, nextPos, _))
          .map(fillCell(sudoku, nextPos, _))
          .map(solve)
          .find(_.isDefined)
          .flatten
    }
}
