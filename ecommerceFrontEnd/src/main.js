// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import VCharts from 'v-charts'
import mavonEditor from 'mavon-editor'
import 'mavon-editor/dist/css/index.css'
// import axios from 'axios'
import App from './App'
import router from './router'
import store from "./store/index";
import locale from 'element-ui/lib/locale/lang/en'
import { post, get, patch, put } from './utils/http'




Vue.prototype.$post = post;
Vue.prototype.$get = get;
Vue.prototype.$patch = patch;
Vue.prototype.$put = put;
Vue.prototype.$isCollapse = store;
Vue.use(mavonEditor)
Vue.use(VCharts)
Vue.use(ElementUI, { locale });
Vue.config.productionTip = false
// Vue.prototype.$axios = axios;
/* eslint-disable no-new */
import {setCookie,getCookie,delCookie} from'./components/cookies.js'   //引用
//Vue.prototype.$setCookie = setCookie;
Vue.prototype.$getCookie = getCookie;
Vue.prototype.$delCookie = delCookie;

new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>',
  render: h => h(App),
  data: function(){
    return {
      user_id: '',
    }
  }
})



router.beforeEach(function (to, from, next) {
  // if (to.path=="/main") {

    // if (auth.getAdminInfo().userUuid) {
    if (!sessionStorage.getItem("isLogin")) {
      next({
        path: '/login'
      });
      
    } else {
      var a = to.path.split("/");
      var d = sessionStorage.getItem("role_id");

      if((a[1]=="Seller"||a[1]=="BVO")&&d!="0"){
        if(a[1]=="Seller"&&d=="1" || a[1]=="BVO"&&d=="2"){
          next();
        }
        else{
          next({
            path: '/login'
          });
        }
      }
      else{
        next();
      }
      
      
    }
  // } else {

  //   next();
  // }
});