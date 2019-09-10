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

mutation CreateFMovement($datetime: DateTime!, $from: Int!, $to: Int!) {
  createFinancialMovement(datetime: $datetime, from: $from, to: $to)
},{
    "datetime": "2019-09-08T21:39:52.329+03:00",
    "from": 1,
    "to": 1
  }