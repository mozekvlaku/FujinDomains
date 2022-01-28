<template>
  <div id="app">
    <Sidebar :domain="activeDomain" :isDomain="searchingDomain" :active="activePage" :sidebarReduced="sideRed"/>
    <div :class="(sideRed) ? 'router-content collapsed' : 'router-content'">
      <router-view></router-view>
    </div>
  </div>
</template>

<script>
import Sidebar from '@/components/Sidebar.vue';
  const DEFAULT_TRANSITION = 'slide';
  export default {
    name: 'FujinDomainsAdministration',
    components: {
      Sidebar
    },
    data() {
      return {
        transitionName: DEFAULT_TRANSITION,
        active: "domains",
        activeDomain: {},
        searchingDomain: false,
        activePage: "",
        sideRed: false
      };
    },
    created() {
      this.$Progress.start();
      this.$router.beforeEach((to, from, next) => {
        if (to.meta.progress !== undefined) {
          let meta = to.meta.progress;
          this.$Progress.parseMeta(meta);
        }
        this.$Progress.start();
        this.expanded = false;
        next();
      });

    },
    methods: {
      checkSideRed() {
        this.sideRed = !this.sideRed;
        this.Sidebar.sidebarReduced = this.sideRed;
      }
    }
  }
</script>