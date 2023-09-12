import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)
const originalPush = Router.prototype.push
Router.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(err => err)
}

export default new Router({
  routes: [
    {
      path: '/',
      redirect: '/login'
    },
    {
      path: '/login',
      component: resolve => require(['../components/Login.vue'], resolve)
    },
    {
      path: '/main',
      component: resolve => require(['../components/common/Main.vue'], resolve),
      children:[
        {
          path: '/',
          component: resolve => require(['../components/page/Home.vue'], resolve)
        },
        {
          path: '/my',
          component: resolve => require(['../components/page/My.vue'], resolve)
        },
        {
          path: '/company',
          component: resolve => require(['../components/page/Seller/Company.vue'], resolve)
        },
        {
          path: '/goods',
          component: resolve => require(['../components/page/Seller/Goods.vue'], resolve)
        },
        {
          path: '/Seller/order',
          component: resolve => require(['../components/page/Seller/Order.vue'], resolve)
        },
        {
          path: '/Seller/wallet',
          component: resolve => require(['../components/page/Seller/Wallet.vue'], resolve)
        },
        {
          path: '/BVO/order',
          component: resolve => require(['../components/page/User/Order.vue'], resolve)
        },
        {
          path: '/BVO/wallet',
          component: resolve => require(['../components/page/User/Wallet.vue'], resolve)
        },
        {
          path: '/store',
          component: resolve => require(['../components/page/User/Store.vue'], resolve)
        },
        {
          path: '/goodslist',
          component: resolve => require(['../components/page/User/GoodsList.vue'], resolve)
        },
        {
          path: '/cart',
          component: resolve => require(['../components/page/User/Cart.vue'], resolve)
        },
        {
          path: '/menu',
          component: resolve => require(['../components/page/Admin/Menu.vue'], resolve)
        },
        {
          path: '/user',
          component: resolve => require(['../components/page/Admin/User.vue'], resolve)
        },
        {
          path: '/param',
          component: resolve => require(['../components/page/Admin/Param.vue'], resolve)
        },
        {
          path: '/data',
          component: resolve => require(['../components/page/Admin/Data.vue'], resolve)
        },
        {
          path: '/check',
          component: resolve => require(['../components/page/Admin/Check.vue'], resolve)
        },
      ]
    }
  ]
})
