var express = require('express')
var router = express.Router()

// 导入请求
const organizationSetup = require('../../network/home/organizationSetup.js')

// 获取机构设置链接
router.get('/:url', function (req, res, next) {
  organizationSetup
    .getOrganizationContent(req.params.url)
    .then(value => {
      res.json(value)
    })
    .catch(reason => {
      res.send(reason)
    })
})

module.exports = router
