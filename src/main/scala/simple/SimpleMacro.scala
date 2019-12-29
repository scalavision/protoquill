package simple

import scala.quoted._
import scala.annotation.StaticAnnotation
import printer.AstPrinter._
import printer.ContextAstPrinter._

object SimpleMacro {
  inline def printThenRun[T](print: String, thenRun: => T): T = ${ printThenRunImpl('print, 'thenRun) }
  def printThenRunImpl[T](print: Expr[String], thenRun: Expr[T])(given qctx: QuoteContext) = {
    import qctx.tasty.{_, given} //Type => _,
    
    print.unseal.underlyingArgument match {
      case Literal(Constant(value)) => println(value)
      case _ => throw new RuntimeException("Needs a literal to be passed to the print method")
    }
    val output = thenRun
    output
  }

  //inline def printThenRun[T](print: String, thenRun: => T): T = {
  //  println(print)
  //  thenRun
  //}oo

  import dotty.tools.dotc.core.tasty.TastyPrinter

  inline def betaReduceMethod(f: Int => Int ):Unit = ${betaReduceMethodImpl('f)}
  def betaReduceMethodImpl(f: Expr[Int => Int])(given qctx: QuoteContext): Expr[Int] = {
    import qctx.tasty.{_, given}

    val reduced = Expr.betaReduce(f)('{123}) //hello
    println(astprint(reduced.unseal.underlyingArgument))
    println(reduced.show)
    reduced
  }
    

  //   '{()}
  // }

  inline def stuff[T](str: T):T = ${ stuffImpl('str) }
  def stuffImpl[T](str: Expr[T])(given qctx: QuoteContext): Expr[T] = {
    import qctx.tasty.{_, given} //Type => _, 
    val und = str.unseal.underlyingArgument


    def splitPrint(str: String) = {
      val result = 
        str.split("\n").foldLeft(("--> ", List[String]())) { 
          case ((str, list), op) => ("    ", (str + op) +: list) 
        }._2.reverse.mkString("\n")
      println(result)
    }

    splitPrint(contextAstPrinter.apply(und).render)  //.showExtractors
    println(str.unseal.underlyingArgument.show)
    //TastyPrinter()
    
    str
  }
}