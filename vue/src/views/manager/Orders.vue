<template>
  <div>
<!--   管理员在后台是不能删除、编辑、添加订单的；可以查询订单（分页查询），‘编辑’改成’发货‘ -->
    <div class="search">
      <el-input placeholder="请输入订单编号" style="width: 200px" v-model="orderId"></el-input>
      <!--      创建了一个下拉选择框，用户可以从“待发货”和“已发货”这两个选项中选择一个，选择的结果会与 Vue 实例中的 status 属性同步。-->
      <!--<el-select> 是 Element UI 中的选择器组件，用于创建下拉选择框。
      v-model="status"：这是一个 Vue 的双向数据绑定。这意味着选择框的值将与 Vue 实例中的 status 属性保持同步。
      当用户在下拉框中选择一个选项时，status 的值将被更新为所选选项的值。
      反之，如果 status 的值在 Vue 实例中被更改，下拉框的显示内容也会相应地更新。-->
      <el-select v-model="status" placeholder="请选择订单状态" style="width: 200px; margin-left: 5px">
        <el-option label="待发货" value="待发货"></el-option>
        <el-option label="已发货" value="已发货"></el-option>
      </el-select>
      <el-button type="info" plain style="margin-left: 10px" @click="load(1)">查询</el-button>
      <el-button type="warning" plain style="margin-left: 10px" @click="reset">重置</el-button>
    </div>

    <div class="table">
      <el-table :data="tableData" stripe>
<!--        后台数据渲染-->
<!--        Sortable — 是一个 JavaScript 库，用于在现代浏览器和触摸设备上对拖放列表进行重新排序。无需 jQuery。支持 Meteor、AngularJS、React、Polymer、Vue、Ember、Knockout 和任何 CSS 库，例如 Bootstrap。-->
        <el-table-column prop="id" label="序号" width="80" align="center" sortable></el-table-column>
<!--        这里的商品图片、商品名称和下单用户要做一个关联查询，因为此时数据库订单信息表只有goods_id和user_id这两个字段-->
        <el-table-column prop="img" label="商品图片" width="100"> <!--这里给定宽度后其他列会自己进行调整-->
          <template v-slot="scope">
            <div style="display: flex; align-items: center">
              <el-image style="width: 40px; height: 40px" v-if="scope.row.img"
                        :src="scope.row.img" :preview-src-list="[scope.row.img]"></el-image>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="goodsName" label="商品名称" show-overflow-tooltip></el-table-column> <!--show-overflow-tooltip ： Element UI表格组件的一个属性，当表格列内容过长时，可以显示省略号和tooltip提示。-->
        <el-table-column prop="price" label="订单价格"></el-table-column>
        <el-table-column prop="orderId" label="订单编号"></el-table-column>
        <el-table-column prop="userName" label="下单用户"></el-table-column>
        <el-table-column prop="time" label="下单时间"></el-table-column>
        <el-table-column prop="status" label="订单状态" width="100"></el-table-column>

        <el-table-column label="操作" width="180" align="center">
          <template v-slot="scope">
<!--          v-if="scope.row.status === '待发货'" ：如果状态是待发货才显示这个按钮
          定义了一个 Element UI 的按钮。只有当 scope.row.status 为 '待发货' 时，这个按钮才会显示。当这个按钮被点击时，它会触发一个方法来改变状态为 '已发货'。-->
            <el-button plain type="primary" size="mini" v-if="scope.row.status === '待发货'" @click="changeStatus(scope.row, '已发货')">发货</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination
            background
            @current-change="handleCurrentChange"
            :current-page="pageNum"
            :page-sizes="[5, 10, 20]"
            :page-size="pageSize"
            layout="total, prev, pager, next"
            :total="total">
        </el-pagination>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "Orders",
  data() {
    return {
      tableData: [],  // 所有的数据
      pageNum: 1,   // 当前的页码
      pageSize: 5,  // 每页显示的个数
      total: 0,
      orderId: null,  //根据订单id编号查询订单，这里是定义订单编号初始化
      status: null,  //根据订单状态查询订单信息，这里是定义订单状态初始化
      fromVisible: false,
      form: {},
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
    }
  },
  created() {
    this.load(1)
  },
  methods: {
    /*定义发货按钮的点击事件*/
    changeStatus(row, status) {
      let data = JSON.parse(JSON.stringify(row))  /*data里面先拿到一行row数据。这里使用了 JavaScript 的 JSON.stringify 和 JSON.parse 方法来创建一个 row 的深拷贝。
      这样做的目的是为了防止直接修改原始数据，因为 row 可能是一个对象或数组，直接修改它的属性或元素可能会导致原始数据被更改。*/
      data.status = status /*更新状态为上面传过来的参数：’已发货‘*/
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
      this.$request.put('/orders/update', data).then(res => {
        if (res.code === '200') { /*检查响应对象 res 的 code 属性是否为 '200'。HTTP 状态码 '200' 表示请求成功。*/
          this.$message.success('操作成功') /*如果请求成功，使用 $message（另一个 Vue 插件， element-ui 的消息提示工具）显示一个“操作成功”的消息。*/
          this.load(1) /*调用当前 Vue 实例上的 load 方法，并传递参数 1。重新加载/刷新页面*/
        } else {
          this.$message.error(res.msg) /*使用 $message 显示一个错误消息，该消息来自响应对象 res 的 msg 属性。*/
        }
      })
    },
    load(pageNum) {  // 分页查询
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/orders/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          orderId: this.orderId,  //加载分页查询方法的时候把orderId带过去，用户输入什么就是什么
          status: this.status,  //加载分页查询方法的时候带过去
        }
      }).then(res => {
        this.tableData = res.data?.list
        this.total = res.data?.total
      })
    },
    reset() {
      this.orderId = null  //重置的时候在把它置空
      this.status = null
      this.load(1)
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum)
    },
  }
}
</script>

<style scoped>

</style>
