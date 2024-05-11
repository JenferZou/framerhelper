import Vue from 'vue'
import VueRouter from 'vue-router'
//  路由
Vue.use(VueRouter)

// 解决导航栏或者底部导航tabBar中的vue-router在3.0版本以上频繁点击菜单报错的问题。
const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(location) {
    return originalPush.call(this, location).catch(err => err)
}

const routes = [
    {
        path: '/',
        name: 'Manager',
        component: () => import('../views/Manager.vue'),
        redirect: '/front/home',  // 重定向到登录页面
        children: [
            {
                path: 'admin/home',
                name: 'Home',
                meta: {name: '系统首页'},
                component: () => import('../views/manager/Home')
            },
            {
                path: 'admin',
                name: 'Admin',
                meta: {name: '管理员信息'},
                component: () => import('../views/manager/Admin')
            },
            {
                path: '/admin/user',
                name: 'User',
                meta: {name: '用户信息'},
                component: () => import('../views/manager/User')
            },
            {
                path: 'admin/adminPerson',
                name: 'AdminPerson',
                meta: {name: '个人信息'},
                component: () => import('../views/manager/AdminPerson')
            },
            {
                path: 'admin/password',
                name: 'Password',
                meta: {name: '修改密码'},
                component: () => import('../views/manager/Password')
            },
            {
                path: 'admin/notice',
                name: 'Notice',
                meta: {name: '公告信息'},
                component: () => import('../views/manager/Notice')
            },
            {
                path: 'admin/information',
                name: 'Information',
                meta: {name: '扶贫资讯'},
                component: () => import('../views/manager/Information')
            },
            {
                path: 'admin/policy',
                name: 'Policy',
                meta: {name: '扶贫政策'},
                component: () => import('../views/manager/Policy')
            },
            {
                path: 'admin/agritainment',
                name: 'Agritainment',
                meta: {name: '农家乐信息'},
                component: () => import('../views/manager/Agritainment')
            },
            {
                path: '/admin/goods',
                name: 'Goods',
                meta: {name: '扶贫商品'},
                component: () => import('../views/manager/Goods')
            },
            {
                path: 'admin/orders',
                name: 'Orders',
                meta: {name: '订单信息'},
                component: () => import('../views/manager/Orders')
            },
            {
                path: 'admin/poorApply',
                name: 'PoorApply',
                meta: {name: '贫困户申请'},
                component: () => import('../views/manager/PoorApply')
            },
            {
                path: 'admin/projectApply',
                name: 'ProjectApply',
                meta: {name: '扶贫项目申请'},
                component: () => import('../views/manager/ProjectApply')
            },
            {
                path: 'admin/comment',
                name: 'Comment',
                meta: {name: '政策评论'},
                component: () => import('../views/manager/Comment')
            },
        ]
    },
    {
        path: '/front',
        name: 'Front',
        component: () => import('../views/Front.vue'),
        children: [
            {path: 'home', name: 'Home', meta: {name: '系统首页'}, component: () => import('../views/front/Home')},
            {
                path: 'person',
                name: 'Person',
                meta: {name: '个人信息'},
                component: () => import('../views/front/Person')
            },
            {
                path: 'informationDetail',
                name: 'InformationDetail',
                meta: {name: '资讯详情'},
                component: () => import('../views/front/InformationDetail')
            },{
                path: 'informationList',
                name: 'InformationList',
                meta: {name:'资讯列表'},
                component: () => import('../views/front/InformationList')
            },
            {
                path: 'policyDetail',
                name: 'PolicyDetail',
                meta: {name: '政策详情'},
                component: () => import('../views/front/PolicyDetail')
            },
            {
                path: 'policyList',
                name: 'PolicyList',
                meta: {name:'政策列表'},
                component: () => import('../views/front/PolicyList')
            },
            {
                path: 'agritainmentDetail',
                name: 'AgritainmentDetail',
                meta: {name: '农家乐详情'},
                component: () => import('../views/front/AgritainmentDetail')
            },
            {
                path: 'orders',
                name: 'Orders',
                meta: {name: '订单信息'},
                component: () => import('../views/front/Orders')
            },
            {path: 'apply', name: 'Apply', meta: {name: '我的申请'}, component: () => import('../views/front/Apply')},
            {
                path: 'praise',
                name: 'Praise',
                meta: {name: '历史点赞'},
                component: () => import('../views/front/Praise')
            },
        ]
    },
    {path: '/login', name: 'Login', meta: {name: '登录'}, component: () => import('../views/Login.vue')},
    {path: '/register', name: 'Register', meta: {name: '注册'}, component: () => import('../views/Register.vue')},
    {path: '*', name: 'NotFound', meta: {name: '无法访问'}, component: () => import('../views/404.vue')},
    {path: '/noauth', name: 'NoAuth', meta: {name: '无权限'}, component: () => import('../views/manager/403')},
]

const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
})

//如果跳转路径包含/admin，则验证该用户是否具有是管理员，如果是就可以跳转，不是就跳转到*
router.beforeEach((to, from, next) => {
    let user = JSON.parse(localStorage.getItem("xm-user") || '{}');
    if (to.path.indexOf('/admin') !== -1) {
        if (user.role === 'ADMIN') {
            next()
        } else {
            next('/noauth')
        }
    } else {
        next()
    }
})


export default router
