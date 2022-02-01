import VueRouter from 'vue-router'
const routes = [
  {
    path: "/",
    name: "Login",
    props: route => ({ id: route.query.returnurl }),
    component: () => import(/* webpackChunkName: "login" */ "../App.vue"),
},
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
