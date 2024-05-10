<!--前台首页-->
<!--传一个id，接收一个id，渲染一个id-->
<template>
  <div class="main-content">
    <div style="width: 65%; margin: 20px auto">
      <div style="text-align: center">
        <div style="text-align: left"><a href="/">&lt返回主页</a></div>
        <span style="font-size: 22px">{{ agritainmentData.name }}</span>
        <span style="margin-left: 50px; color: #666666">发布时间：{{ agritainmentData.time }}</span>
      </div>
      <div v-html="agritainmentData.content" class="w-e-text w-e-text-container" style="margin-top: 30px"></div>
    </div>
  </div>
</template>

<script>
import E from 'wangeditor'
export default {

  data() {
    let agritainmentId = this.$route.query.id  /*拿到id*/
    return {
      agritainmentId: agritainmentId,
      agritainmentData: {}
    }
  },
  mounted() {
    this.loadInformation()
  },
  // methods：本页面所有的点击事件或者其他函数定义区
  methods: {
    loadInformation() {
      //ES6允许使用“箭头”（=>）定义函数
      /*var f = a = > a等同于
        var f = function(a){
          return a;
        }
       **************************
       如果箭头函数不需要参数或需要多个参数，就使用一个圆括号代表参数部分。
       *******************无形参*******************
        var f = () => 5;等同于var f = function () { return 5 };
       *******************多个形参*******************
        var sum = (num1, num2) => num1 + num2;等同于
        var sum = function(num1, num2) {
            return num1 + num2;
         };
      */
      this.$request.get('/agritainment/selectById/' + this.agritainmentId).then(res => {
        if (res.code === '200') {
          this.agritainmentData = res.data
        } else {
          this.$message.error(res.msg)
        }
      })
    }
  }
}
</script>

<style>
p, ol {
  color: #3a3c40 !important;
  line-height: 30px !important;
}
</style>
