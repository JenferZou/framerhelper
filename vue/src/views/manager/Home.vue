<template>
  <div>
    <div class="card" style="padding: 15px">
      您好，{{ user?.name }}！欢迎使用本系统
    </div>

    <div style="display: flex; margin: 10px 0">
      <div style="width: 50%;" class="card">
        <div style="margin-bottom: 30px; font-size: 20px; font-weight: bold">公告列表</div>
        <div >
          <el-timeline  reverse slot="reference">
            <el-timeline-item v-for="item in notices" :key="item.id" :timestamp="item.time">
              <el-popover
                  placement="right"
                  width="200px"
                  trigger="hover"
                  :content="item.content">
                <span slot="reference">{{ item.title }}</span>
              </el-popover>
            </el-timeline-item>
          </el-timeline>
        </div>
      </div>
<!--      <div style="width: 40%;margin-left: 20px;height: 400px" id="chart"></div>-->
    </div>
  </div>
</template>

<script>

// 导入Echarts库
import * as echarts from "echarts";
export default {
  name: 'Home',
  data() {
    return {
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      notices: [],
      chartDom: "",
      myChart: "",
      option: "",
      countU:[],
      temp:[]
    };
  },
  created() {
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
    this.$request.get('/notice/selectAll').then(res => {
      this.notices = res.data || []
    });
    /*this.$request.get('/user/countUser').then(res => {
      console.log(res)
      this.countU=res
      this.chartDom = document.getElementById("chart");
      this.myChart = echarts.init(this.chartDom);
      this.initCharts();
    });*/
  },
  mounted () {
    this.$nextTick(() => {
    });
  },
  methods: {
    initCharts () {
      this.option = {
        title: {
          text: '系统用户类型比例',
          left: 'center'
        },
        tooltip: {
          trigger: 'item'
        },
        legend: {
          orient: 'vertical',  //设置布局方式为垂直
          left: 'left'
        },
        series: [{
          name: '人数',
          type: 'pie',
          radius: '50%',
          data: this.countU,
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          }
        }]
      };
      this.option && this.myChart.setOption(this.option);
    },
  },
}
</script>
