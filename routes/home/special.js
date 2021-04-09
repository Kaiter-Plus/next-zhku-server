var express = require('express')
var router = express.Router()

// 导入请求
const special = require('../../network/home/special.js')

// 获取专题标题
router.get('/', function (req, res, next) {
  special
    .getSpecialTitle('ztbd.htm')
    .then(value => {
      res.json(value)
    })
    .catch(reason => {
      res.send(reason)
    })
})

// 获取专题列表
router.get('/:url', function (req, res, next) {
  special
    .getSpecialList(req.params.url)
    .then(value => {
      res.json(value)
    })
    .catch(reason => {
      res.send(reason)
    })
})

// 获取专题内容

module.exports = router
