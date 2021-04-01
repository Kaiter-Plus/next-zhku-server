var express = require('express')
var router = express.Router()

// 导入请求
const footer = require('../../network/home/footer.js')

router.get('/', function (req, res, next) {
  footer
    .getVisits()
    .then(value => {
      res.json(value)
    })
    .catch(reason => {
      res.write(reason)
    })
})

module.exports = router
