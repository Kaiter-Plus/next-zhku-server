const a = require('./redis/index.js')

a.get('specialList', function (r) {
  console.log(r)
})
