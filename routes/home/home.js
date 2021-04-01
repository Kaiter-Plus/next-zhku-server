var express = require('express')
var router = express.Router()

// 导入请求
const getHomeJson = require('../../network/home/home.js')

// 获取主页数据
router.get('/', function (req, res, next) {
  getHomeJson()
    .then(value => {
      res.json(value)
    })
    .catch(reason => {
      res.write(reason)
    })
})

module.exports = router
