<!--前台首页-->
<!--传一个id，接收一个id，渲染一个id-->
<template>
  <div class="main-content">
    <div style="width: 65%; margin: 20px auto">
      <div><a href="/">&lt返回主页</a></div>
      <div style="text-align: center">
        <span style="font-size: 22px">{{ informationData.name }}</span>
        <span style="margin-left: 50px; color: #666666">{{ informationData.time }}</span>
      </div>
      <div v-html="informationData.content" class="w-e-text w-e-text-container" style="margin-top: 30px"></div>
    </div>
  </div>
</template>

<script>
import E from 'wangeditor'
export default {

  data() {
    let informationId = this.$route.query.id /*拿到页面上的id*/
    return {
      informationId: informationId,
      informationData: {}
    }
  },
  mounted() {
    this.loadInformation()
  },
  // methods：本页面所有的点击事件或者其他函数定义区
  methods: {
    loadInformation() {/*调用后端接口，获取页面id*/
      this.$request.get('/information/selectById/' + this.informationId).then(res => {
        if (res.code === '200') {  /*返回值是200，表示成功拿到数据 ，拿到后赋值到页面上*/
          this.informationData = res.data
        } else {
          this.$message.error(res.msg)  /*报错打印*/
        }
      })
    }
  }
}
</script>

<style>
p {
  color: #3a3c40 !important;
  line-height: 30px !important;
}
</style>
