import Vue from 'vue'
import VueRouter from 'vue-router'
import Domains from '../views/Domains.vue'
import MainPage from '../views/MainPage.vue'
import Domain from '../views/Domain.vue'
import DomainUsers from '../views/DomainUsers.vue'
import DomainUser from '../views/DomainUser.vue'
import DomainGroup from '../views/DomainGroup.vue'
import DomainGroupNew from '../views/DomainGroupNew.vue'
import DomainUserNew from '../views/DomainUserNew.vue'
import DomainGroups from '../views/DomainGroups.vue'
import Schema from '../views/Schema.vue'
import Server from '../views/Server.vue'
import Security from '../views/Security.vue'
import Export from '../views/Export.vue'
import DomainNew from '../views/DomainNew.vue'
import MyFederations from '../views/MyFederations.vue'
import SearchResults from '../views/SearchResults.vue'
import E404 from '../views/404.vue'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Domov',
    component: MainPage
  },
  {
    path: '/domain-list',
    name: 'Seznam domén',
    component: Domains
  },{
    path: '/server',
    name: 'Server',
    component: Server
  },{
    path: '/federations',
    name: 'Mé federace',
    component: MyFederations
  },{
    path: '/security',
    name: 'Zabezpečení',
    component: Security
  },
  {
    path: '/domain/:domainName',
    name: 'Doména',
    component: Domain,
    props: (route) => ({domainName: Vue.observable(new Counter(route.params.domainName))}),
  },{
    path: '/domain/:domainName/search/:query',
    name: 'Hledání',
    component: SearchResults,
    props: (route) => ({domainName: Vue.observable(new Counter(route.params))}),
  },
   {
    path: '/domain/:domainName/users',
    name: 'Uživatelé v doméně',
    component: DomainUsers,
    props: (route) => ({domainName: Vue.observable(new Counter(route.params.domainName))}),
  },{
    path: '/domain/:domainName/user/:userDn',
    name: 'Uživatel v doméně',
    component: DomainUser,
    props: (route) => ({domainName: Vue.observable(new Counter(route.params)), deleting: false}),
  },{
    path: '/domain/:domainName/group/:groupDn',
    name: 'Skupina v doméně',
    component: DomainGroup,
    props: (route) => ({domainName: Vue.observable(new Counter(route.params)), deleting: false}),
  },{
    path: '/domain/:domainName/group_d/:groupDn',
    name: 'Skupina v doméně',
    component: DomainGroup,
    props: (route) => ({domainName: Vue.observable(new Counter(route.params)), deleting: true}),
  },{
    path: '/domain/:domainName/new_user',
    name: 'Nový uživatel v doméně',
    component: DomainUserNew,
    props: (route) => ({domainName: Vue.observable(new Counter(route.params))}),
  },{
    path: '/domain/:domainName/export',
    name: 'Export',
    component: Export
  },{
    path: '/domain/:domainName/new_group',
    name: 'Nová skupina v doméně',
    component: DomainGroupNew,
    props: (route) => ({domainName: Vue.observable(new Counter(route.params))}),
  },{
    path: '/new_domain',
    name: 'Nová doména',
    component: DomainNew
  },{
    path: '/domain/:domainName/user_d/:userDn',
    name: 'Uživatel v doméně',
    component: DomainUser,
    props: (route) => ({domainName: Vue.observable(new Counter(route.params)), deleting: true}),
  },{
    path: '/domain/:domainName/groups',
    name: 'Skupiny v doméně',
    component: DomainGroups,
    props: (route) => ({domainName: Vue.observable(new Counter(route.params.domainName))}),
  },
  {
    path: '/domain/:domainName/schema',
    name: 'Schéma domény',
    component: Schema,
    props: (route) => ({domainName: Vue.observable(new Counter(route.params.domainName))}),
  },
  {
    path: '*',
    name: 'Nenalezeno',
    component: E404
  },
]

class Counter {
  constructor(value) {
    this.domainName = value
  }
}

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})




export default router
