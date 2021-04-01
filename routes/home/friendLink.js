var express = require('express')
var router = express.Router()

// 导入请求
const friendLink = require('../../network/home/friendLink.js')

// 获取友情链接
router.get('/', function (req, res, next) {
  friendLink
    .getFriendLink()
    .then(value => {
      res.json(value)
    })
    .catch(reason => {
      res.write(reason)
    })
})

module.exports = router
