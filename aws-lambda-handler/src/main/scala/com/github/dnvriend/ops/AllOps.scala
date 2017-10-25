package com.github.dnvriend.ops

object AllOps extends AllOps

trait AllOps extends ByteArrayOps
  with ByteBufferOps
  with InputStreamOps
  with OutputStreamOps
  with StringOps
  with ContextOps
  with ApiGatewayOps
  with AvroOps
