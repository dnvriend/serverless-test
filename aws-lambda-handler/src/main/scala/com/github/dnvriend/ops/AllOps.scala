package com.github.dnvriend.ops

object AllOps extends AllOps

trait AllOps extends ApiGatewayOps
  with AvroOps
  with ByteArrayOps
  with ByteBufferOps
  with ContextOps
  with FunctionalOps
  with InputStreamOps
  with JsonOps
  with OutputStreamOps
  with StringOps
