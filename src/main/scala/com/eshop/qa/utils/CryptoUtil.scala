package com.eshop.qa.utils

import com.qaprosoft.carina.core.foundation.commons.SpecialKeywords
import com.qaprosoft.carina.core.foundation.crypto.{CryptoConsole, CryptoTool}

import java.util.regex.Pattern
import scala.io.Source

object CryptoUtil {
  val cryptoKeyPath = System.getProperty("CRYPTO_KEY_PATH", "src/test/resources/crypto.key")
  val cryptoTool: CryptoTool = new CryptoTool(cryptoKeyPath)

  val cryptoPattern: Pattern = Pattern.compile(SpecialKeywords.CRYPT)

  def decrypt(str: String): String = {
    println(s"Crypto Key Path: $cryptoKeyPath")
    cryptoTool.decryptByPattern(str, cryptoPattern)
  }

  def encrypt(str: String): String = cryptoTool.encrypt(str)

  def main(args: Array[String]): Unit = {
    println(encrypt(args.lastOption.get))
    CryptoConsole.main(args)
  }
}