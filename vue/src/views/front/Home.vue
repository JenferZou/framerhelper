<!--前台首页-->
<template>
  <div class="main-content">
    <div>
      <!--    顶部轮播图 ： Element->Carousel走马灯   -->
      <el-carousel :interval="4000" type="card" height="300px"> <!--interval="4000" : 4秒转换一次-->
        <el-carousel-item v-for="item in carouselData" :key="item">
          <!--v-for="item in carouselDate" 轮播图片的数量由carouselDate方法中的图片数量决定-->
          <img :src="item" alt="" style="width: 100%; height: 300px; border-radius: 5px">
          <!-- border-radius：圆边-->
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
              <div style="display: flex; line-height: 45px; height: 45px" v-for="item in informationData">
                <!--              这里flex占一份，下面给定，剩下的全是这一部分的-->
                <!--              需要文字过长时自动省略，调用方法函数，取名overflowShow-->
                <!--渲染：扶贫资讯数据 别名informationData-->
                <div style="flex: 1; font-size: 17px; width: 0;"
                     class="overflowShow" @click="navTo('/front/informationDetail?id=' + item.id)">
                  {{ item.name }}
                </div>
                <div style="width: 150px; text-align: right; color: #a9a9b8; font-size: 15px">
                  {{ item.time }}
                </div>
              </div>
              <div style="display: flex; line-height: 35px; height: 35px;margin-right: 12px">
                <div style="flex: 1"></div>
                <div style="width: 150px; text-align: right; color: red; font-size: 15px">
                  <a class="seeMore" @click="navTo('/front/informationList')">查看更多</a>
                </div>
              </div>
            </el-tab-pane>

            <!--         text-align: right" ； 文字靠右-->
            <el-tab-pane label="扶贫政策" name="second">
              <div style="display: flex; line-height: 45px; height: 45px" v-for="item in policyData">
                <div style="flex: 1; font-size: 17px; width: 0;"
                     class="overflowShow" @click="navTo('/front/policyDetail?id=' + item.id)">
                  {{ item.name }}
                </div>
                <div style="width: 150px; text-align: right; color: #a9a9b8; font-size: 15px">
                  {{ item.time }}
                </div>
              </div>
              <div style="display: flex; line-height: 35px; height: 35px;margin-right: 12px">
                <div style="flex: 1"></div>
                <div style="width: 150px; text-align: right; color: red; font-size: 15px;">
                  <a class="seeMore" @click="navTo('/front/policyList')">查看更多</a>
                </div>
              </div>
            </el-tab-pane>
          </el-tabs>
        </div>  <!--这里flex占一份-->
      </div>


      <div style="display: flex">
        <div style="width: 70%">
          <!--    第三块 扶贫案例-->
          <div style="text-align: left; color: #19a14b; font-size: 25px; font-weight: 550; margin: 40px 15px">
            扶贫案例
          </div>
          <el-row :gutter="20">
            <el-col :span="6" style="margin-bottom: 50px" v-for="item in agritainmentData"> <!--分三份 3*8=24 -->
              <!--每一列col 都去做一个for循环，放在取名item里面-->
              <!--       点击图片就能跳转，在图片上进行操作：  cursor: pointer 鼠标移动到图片上会出现小手指   ;  @click="navTo('/front/agritainmentDetail?id=' + item.id)：点击跳转agritainmentDetail前台页面（记得index.js加路由），传入id -->
              <img :src="item.img" alt=""
                   style="width: 100%; height: 180px; border-radius: 10px; cursor: pointer;"
                   @click="navTo('/front/agritainmentDetail?id=' + item.id)">
              <div style="text-align: center; font-size: 16px; font-weight: 530; margin-top: 10px">
                {{ item.name }}
              </div>
            </el-col>
          </el-row>

        </div>
        <div style="width: 30%">
          <!--    第四块 公共栏-->
          <div
              style="text-align: right; color: #19a14b; font-size: 25px; font-weight: 550; margin: 40px 40px 30px 0;padding-right: 60px">
            公告栏
          </div>
          <div style="margin-left: 80px">
            <el-timeline reverse slot="reference">
              <el-timeline-item v-for="item in notices" :key="item.id" :timestamp="item.time">
                <el-popover
                    placement="right"
                    width="200px"
                    trigger="hover">
<!--                    :content="item.content">-->
                  <span slot="reference">{{ item.title }}:{{item.content}}</span>
                </el-popover>
              </el-timeline-item>
            </el-timeline>
          </div>
        </div>
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
      notices: [],
    }
  },
  mounted() {
    this.loadInformation()
    this.loadPolicy()
    this.loadAgritainment()
  },
  // methods：本页面所有的点击事件或者其他函数定义区
  methods: {
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
  },
  created() {
    this.$request.get('/notice/selectAll').then(res => {
      this.notices = res.data || []
    });
  }
}
</script>

<style scoped>
.overflowShow {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.seeMore:hover {
  color: lightcoral;
  cursor: pointer;
}
</style>
