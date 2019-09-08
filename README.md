https://github.com/joelsaunders/ktor-starter

https://github.com/adavis/ufo-sightings-api/blob/master/src/main/kotlin/info/adavis/graphql/AppSchema.kt
https://www.youtube.com/watch?v=1NmXeZgtkvg

Örnek GraphQL sorguları

query GetSafes($id: Int!) {
  safe(id: $id) {
    code
    name
  }
},
{
  "id" : "2"
}

query GetMovements($id: Int!) {
  fmovement(id: $id) {
    id
    from {
      id
      name
      balance
    }
    to {
      id
      name
    }
  }
},
 {
   "id" : "1"
 }
