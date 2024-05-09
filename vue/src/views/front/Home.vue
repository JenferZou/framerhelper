<!--前台首页-->
<template>
  <div class="main-content">
    <div>
<!--    顶部轮播图 ： Element->Carousel走马灯   -->
      <el-carousel :interval="4000" type="card" height="300px"> <!--interval="4000" : 4秒转换一次-->
        <el-carousel-item v-for="item in carouselData" :key="item"> <!--v-for="item in carouselDate" 轮播图片的数量由carouselDate方法中的图片数量决定-->
          <img :src="item" alt="" style="width: 100%; height: 300px; border-radius: 5px"> <!-- border-radius：圆边-->
        </el-carousel-item>
      </el-carousel>
    </div>
    <div style="width: 80%; margin: 20px auto;">
<!--    第二块 左 图片-->
      <div style="display: flex"> <!--默认两个div上下布局；现在修改成左右布局 ：style="display: flex"-->
        <div style="width: 50%; height: 330px; margin-right: 17px; margin-top: 10px">
          <el-carousel height="330px">
            <el-carousel-item v-for="item in left" :key="item">
              <img :src="item" alt="" style="width: 100%; height: 330px">
            </el-carousel-item>
          </el-carousel>
        </div> <!--这里flex占一份 也可以width：50%-->
<!--     第二块 右 扶贫资讯 Element：Tabs标签页 -->
        <div style="width: 50%; height: 330px; margin-left: 17px">
          <el-tabs v-model="activeName"> <!--activeName与下面选项的name绑定 ; @tab-click="handleClick"是点击切换后的函数-->
            <el-tab-pane label="扶贫资讯" name="first">
              <div style="display: flex; line-height: 48px; height: 48px" v-for="item in informationData">
<!--              这里flex占一份，下面给定，剩下的全是这一部分的-->
<!--              需要文字过长时自动省略，调用方法函数，取名overflowShow-->   <!--渲染：扶贫资讯数据 别名informationData-->
                <div style="flex: 1; font-size: 17px; width: 0; cursor: url(https://ik.imagekit.io/nicexl/cursor/link.cur),default !important;" class="overflowShow" @click="navTo('/front/informationDetail?id=' + item.id)">{{ item.name }}</div>
                <div style="width: 150px; text-align: right; color: #a9a9b8; font-size: 15px">{{ item.time }}</div>
              </div>
            </el-tab-pane>
<!--         text-align: right" ； 文字靠右-->
            <el-tab-pane label="扶贫政策" name="second">
              <div style="display: flex; line-height: 48px; height: 48px" v-for="item in policyData">
                <div style="flex: 1; font-size: 17px; width: 0; cursor: url(https://ik.imagekit.io/nicexl/cursor/link.cur),default !important;" class="overflowShow" @click="navTo('/front/policyDetail?id=' + item.id)">{{ item.name }}</div>
                <div style="width: 150px; text-align: right; color: #a9a9b8; font-size: 15px">{{ item.time }}</div>
              </div>
            </el-tab-pane>
          </el-tabs>
        </div>  <!--这里flex占一份-->
      </div>

      <!--    第三块 魅力农家乐-->
      <div style="text-align: center; color: #19a14b; font-size: 25px; font-weight: 550; margin: 40px 0">魅力农家乐</div>
      <div>
        <el-row :gutter="20">
          <el-col :span="8" style="margin-bottom: 50px" v-for="item in agritainmentData"> <!--分三份 3*8=24 --> <!--每一列col 都去做一个for循环，放在取名item里面-->
<!--       点击图片就能跳转，在图片上进行操作：  cursor: pointer 鼠标移动到图片上会出现小手指   ;  @click="navTo('/front/agritainmentDetail?id=' + item.id)：点击跳转agritainmentDetail前台页面（记得index.js加路由），传入id -->
            <img :src="item.img" alt="" style="width: 100%; height: 180px; border-radius: 10px; cursor: pointer;" @click="navTo('/front/agritainmentDetail?id=' + item.id)">
            <div style="text-align: center; font-size: 16px; font-weight: 530; margin-top: 10px">{{ item.name }}</div>
          </el-col>
        </el-row>
      </div>

<!--    第四块   vue2-->
      <div style="text-align: center; color: #19a14b; font-size: 25px; font-weight: 550">优质农产品</div>
      <div style="height: 1000px">
        <el-row :gutter="20"> <!--:gutter="20"每个框边距20-->
          <el-col :span="4" style="margin-top: 30px" v-for="item in goodsData">
<!--        col:列  ;  :span="4" 一行有六列，4*6=24--><!--每一列for循环每一个（下面已经定义好的）商品数据goodData，用item来表示-->
            <img :src="item.img" alt="" style="width: 100%; height: 180px; border-radius: 10px; border: 1px solid #cccccc"> <!--src前面的冒号少了 图片就加载不出来-->
            <div style="text-align: left; font-weight: 530; font-size: 16px; margin-top: 10px" class="overflowShow">{{ item.name }}</div>
            <div style="margin-top: 10px">
              <span style="color: red; margin-right: 30px">￥ {{ item.price }}</span>
              <span style="color: #666666; margin-right: 10px">库存：{{ item.num }}</span>
            </div>
            <div style="margin-top: 10px">
<!--              添加点击事件以及对应方法，把助农扶贫商品数据传进去-->
<!--              :disabled="item.num === 0"  当库存数量为0时，购买按钮隐藏，不能点击-->
              <el-button type="warning" size="mini" @click="buy(item)" :disabled="item.num === 0">购 买</el-button>
            </div>
          </el-col>
        </el-row>
      </div>
    </div>
  </div>
</template>

<script>

/*import {left} from "core-js/internals/array-reduce";*/
export default {

  data() {
    return {
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'), /*初始化user localStorage缓存数据，登录之前缓存*/
      /*创建轮播图2的变量去循环它*/
      carouselData: [
        /*静态图---然后动态替换*/
          require('@/assets/imgs/多彩贵州.jpg'),
          require('@/assets/imgs/绚烂湛江.jpg'),
          require('@/assets/imgs/腾冲和顺古镇.jpg'),
      ],
      left: [
        require('@/assets/imgs/多彩贵州.jpg'),
        require('@/assets/imgs/绚烂湛江.jpg'),
        require('@/assets/imgs/腾冲和顺古镇.jpg'),
      ], /*渲染方法前先在这里 对应一个 接收相应模块信息的数组  定义方法后循环遍历*/
      activeName: 'first', /*这个方法默认显示name为first这一项*/
      informationData: [],  /*定义 扶贫资讯数据*/
      policyData: [],  /* 扶贫政策的数据*/
      agritainmentData: [],  /*农家乐数据*/
      goodsData: [],  /*助农扶贫商品 数据*/
    }
  },
  mounted() {
    this.loadInformation()
    this.loadPolicy()
    this.loadAgritainment()
    this.loadGoods()  /*把所有商品的数据查出来*/
  },
  // methods：本页面所有的点击事件或者其他函数定义区
  methods: {
    buy(goods) { //原参数item，这里是改用形参goods，更清晰表示传入的是商品数据
      if (this.user.role === 'ADMIN') { //4 可以限制管理员不能购买
        this.$message.warning('您是管理员，不支持该操作')  /*管理员权限检查，显示一个警告消息，并（return）提前结束函数的执行*/
        return
      }
      let data = { //3 初始化 商品数据 ；这里定义了一个名为 data 的对象，用于存储购买商品所需的数据。
        goodsId: goods.id,
        userId: this.user.id, //当前登录的user的id
        price: goods.price, //价格，助农商品，每人购买一次一份，暂时不考虑购买多份计算
        status: '待发货' //商品状态默认是 待发货
      }
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
      //使用 Vue 的 $request 属性（可能是一个 Axios 实例）发送一个 POST 请求到 '/orders/add' 路径，并传递 data 作为请求体。
      this.$request.post('/orders/add', data).then(res => { //1 调用方法后端的post请求 到前台/orders/add方法，前台把data传过来// 1.1 then(res => {...}): 当服务器响应时，执行这个回调函数。
        if (res.code === '200') { //2 调用完后判断状态码 检查服务器的响应状态码是否为 '200'（通常表示成功）。
          //2.1 如果是，则显示一个 '购买成功' 的消息，并调用 this.loadGoods() 方法（可能是重新加载商品列表）。
          this.$message.success('购买成功')
          this.loadGoods()
        } else { //2.2 如果状态码不是 '200'，则显示服务器返回的错误消息。
          this.$message.error(res.msg)
        }
      })
    },
    loadGoods() {
      this.$request.get('/goods/selectAll').then(res => {  /*发送$request.get请求查询所有   then拿到返回值*/
        if (res.code === '200') {  /*拿到返回值后 先去判断状态码*/
          this.goodsData = res.data  /*如果没问题，把数据赋值给goodsData  数据有了之后，我们去返回上面渲染出来*/
        } else {
          this.$message.error(res.msg)  /*有问题 打印出来*/
        }
      })
    },
    loadAgritainment() {
      this.$request.get('/agritainment/top6').then(res => {  /*拿最新的6条*/
        if (res.code === '200') {
          this.agritainmentData = res.data //拿到数据之后，我们到上面去渲染
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    loadInformation() {
      this.$request.get('/information/top6').then(res => {
        if (res.code === '200') {
          this.informationData = res.data
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    loadPolicy() {
      this.$request.get('/policy/top6').then(res => {
        if (res.code === '200') {
          this.policyData = res.data
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    navTo(url) {  //统一各模块共用跳转页面方法
      location.href = url
    }
  }
}
</script>

<style scoped>
.overflowShow {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>
