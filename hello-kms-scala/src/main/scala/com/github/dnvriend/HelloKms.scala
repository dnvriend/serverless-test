package com.github.dnvriend

import com.amazonaws.auth.profile.ProfileCredentialsProvider

import scala.collection.JavaConverters._
import com.amazonaws.encryptionsdk.AwsCrypto
import com.amazonaws.encryptionsdk.CryptoResult
import com.amazonaws.encryptionsdk.kms.KmsMasterKey
import com.amazonaws.encryptionsdk.kms.KmsMasterKeyProvider
import com.amazonaws.regions.Regions
import com.github.dnvriend.ops.AllOps

/**
 * Note: you have to have appropriate policies for your API key user
 * like: kms:GenerateDataKey
 */
object HelloKms extends AllOps {
  def main(args: Array[String]): Unit = {
    val keyId = "94d4bddb-1f7b-49dc-ace2-8ec2f8d7ed0b"
    val keyArn = "arn:aws:kms:us-east-1:436740350302:key/94d4bddb-1f7b-49dc-ace2-8ec2f8d7ed0b"
    val payloadToEncrypt: Array[Byte] = "HelloWorld".arr.unwrap

    // initialize the sdk
    val crypto = new AwsCrypto()
    // Set up the KmsMasterKeyProvider backed by the default credentials
    val provider = new KmsMasterKeyProvider(keyId)

    // Encrypt the data
    val encrypted: CryptoResult[Array[Byte], KmsMasterKey] = crypto.encryptData(provider, payloadToEncrypt)
    val encryptedHex = encrypted.getResult.hex
    encryptedHex.log
    val decrypted: CryptoResult[Array[Byte], KmsMasterKey] = crypto.decryptData(provider, encrypted.getResult)

    decrypted.getResult.str.log
  }
}

/**
 * data -> encrypted
 * data -> key ophalen -> master key ->
 */
