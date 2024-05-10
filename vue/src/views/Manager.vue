<!--菜单主页面-->
<template>
  <div class="manager-container">
    <!--  头部  -->
    <div class="manager-header">
      <div class="manager-header-left">
        <img src="@/assets/imgs/logo.png" />
        <div class="title">扶贫信息网</div>
      </div>

      <div class="manager-header-center">
        <el-breadcrumb separator-class="el-icon-arrow-right">
          <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
          <el-breadcrumb-item :to="{ path: $route.path }">{{ $route.meta.name }}</el-breadcrumb-item>
        </el-breadcrumb>
      </div>

      <div class="manager-header-right">
        <el-dropdown placement="bottom">
          <div class="avatar">
            <img :src="user.avatar || 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'" />
            <div>{{ user.name ||  '管理员' }}</div>
          </div>
          <el-dropdown-menu slot="dropdown">
<!--            <el-dropdown-item @click.native="goToPerson">个人信息</el-dropdown-item>-->
            <el-dropdown-item @click.native="$router.push('/')">前往前台</el-dropdown-item>
            <el-dropdown-item @click.native="logout">退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </div>

    <!--  主体  -->
    <div class="manager-main">
      <!--  侧边栏  -->
      <div class="manager-main-left">
        <el-menu router style="border: none" :default-active="$route.path">
          <el-menu-item index="/admin/home">
            <i class="el-icon-s-home"></i>
            <span slot="title">系统首页</span>
          </el-menu-item>
          <el-submenu index="info">
            <template slot="title">
              <i class="el-icon-s-platform"></i><span>信息管理</span>
            </template>
            <el-menu-item index="/admin/notice">公告信息</el-menu-item>
            <el-menu-item index="/admin/information">扶贫资讯</el-menu-item>
            <el-menu-item index="/admin/policy">扶贫政策</el-menu-item>
            <el-menu-item index="/admin/agritainment">扶贫案例</el-menu-item>
<!--            <el-menu-item index="/admin/comment">政策评论</el-menu-item>-->
          </el-submenu>

<!--          <el-submenu index="apply">-->
<!--            <template slot="title">-->
<!--              <i class="el-icon-s-claim"></i><span>审核申请</span>-->
<!--            </template>-->
<!--            <el-menu-item index="/admin/poorApply">贫困户申请</el-menu-item>-->
<!--            <el-menu-item index="/admin/projectApply">扶贫项目申请</el-menu-item>-->
<!--          </el-submenu>-->

          <el-submenu index="user">
            <template slot="title">
              <i class="el-icon-s-custom"></i><span>用户管理</span>
            </template>
            <el-menu-item index="/admin">管理员信息</el-menu-item>
<!--            <el-menu-item index="/admin/user">用户信息</el-menu-item>-->
          </el-submenu>
        </el-menu>
      </div>

      <!--  数据表格  -->
      <div class="manager-main-right">
        <router-view @update:user="updateUser" />
      </div>
    </div>

  </div>
</template>

<script>
export default {
  name: "Manager",
  data() {
    return {
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
    }
  },
  created() {
    if (!this.user.id) {
      this.$router.push('/login')
    }
  },
  methods: {
    updateUser() {
      this.user = JSON.parse(localStorage.getItem('xm-user') || '{}')   // 重新获取用户的最新信息
    },
    goToPerson() {
      if (this.user.role === 'ADMIN') {
        this.$router.push('/admin/adminPerson')
      }
    },
    logout() {
      localStorage.removeItem('xm-user')
      this.$router.push('/')
      location.reload();
    }
  }
}
</script>

<style scoped>
@import "@/assets/css/manager.css";
</style>