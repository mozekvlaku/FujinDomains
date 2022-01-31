<template>
  <div class="hidden">
    <vs-sidebar absolute v-model="active" open  :reduce="sidebarReduced">
      <template #logo>
        <img v-if="darkmode && !sidebarReduced" src="http://cdn.vespotok.net/img/fujindomainshorizontal.svg" tag="router-link"
          :to="{ path: '/' }" alt="Logo" style="max-width: 190px;">
        <img v-if="!darkmode && !sidebarReduced" src="http://cdn.vespotok.net/img/invfujindomainshorizontal.svg" tag="router-link"
          :to="{ path: '/' }" alt="Logo" style="max-width: 190px;">
          <img v-if="darkmode && sidebarReduced" src="http://cdn.vespotok.net/img/fujindomainscollapsed.svg" tag="router-link"
          :to="{ path: '/' }" alt="Logo" style="max-width: 100px;">
        <img v-if="!darkmode && sidebarReduced" src="http://cdn.vespotok.net/img/invfujindomainscollapsed.svg" tag="router-link"
          :to="{ path: '/' }" alt="Logo" style="max-width: 100px;">
      </template>
      <vs-sidebar-item disabled>
        <template #icon>
          <i class='bx bx-search'></i>
        </template>
        <vs-input primary v-model="value" placeholder="Vyhledávejte v doméně">
        </vs-input>
      </vs-sidebar-item>
      <vs-sidebar-item id="/" tag="router-link" :to="{ path: '/' }">
        <template #icon>
          <i class='bx bx-home'></i>
        </template>
        Domů
      </vs-sidebar-item>
      <vs-sidebar-item id="federations" tag="router-link" :to="{ path: '/federations' }">
        <template #icon>
          <i class='bx bx-shape-triangle'></i>
        </template>
        Mé federace
      </vs-sidebar-item>
      <vs-sidebar-item id="security">
        <template #icon>
          <i class='bx bx-lock-open-alt'></i>
        </template>
        Zabezpečení
      </vs-sidebar-item>
      <vs-sidebar-item id="/domain-list" tag="router-link" :to="{ path: '/domain-list' }">
        <template #icon>
          <i class='bx bx-grid-alt'></i>
        </template>
        Domény
       
      </vs-sidebar-item>
      <vs-sidebar-group v-if="isDomain">
        <template #header>
          <vs-sidebar-item arrow>
            <template #icon>
              <i class='bx bx-group'></i>
            </template>
            {{domain.domainName}}
          </vs-sidebar-item>
        </template>

        <vs-sidebar-item id="domainLink" tag="router-link" :to="{ path: '/domain/' + domain.domainName }">
          <template #icon>
            <i class='bx bx-cylinder'></i>
          </template>
          Doména
        </vs-sidebar-item>
        <vs-sidebar-item id="domainUsers" tag="router-link" :to="{ path: '/domain/'+  domain.domainName+'/users' }">
          <template #icon>
            <i class='bx bx-user'></i>
          </template>
          Uživatelé
        </vs-sidebar-item>
        <vs-sidebar-item id="domainUser" v-if="active == 'domainUser'">
          <template #icon>
            <i class='bx bx-user-circle'></i>
          </template>
          Uživatel
        </vs-sidebar-item>
        <vs-sidebar-item id="domainUserNew" v-if="active == 'domainUserNew'">
          <template #icon>
            <i class='bx bxs-user-plus'></i>
          </template>
          Nový uživatel
        </vs-sidebar-item>
        <vs-sidebar-item id="domainGroups" tag="router-link" :to="{ path: '/domain/'+  domain.domainName+'/groups' }">
          <template #icon>
            <i class='bx bx-group'></i>
          </template>
          Skupiny
        </vs-sidebar-item>
        <vs-sidebar-item id="domainSchema" tag="router-link" :to="{ path: '/domain/'+  domain.domainName+'/schema' }">
          <template #icon>
            <i class='bx bx-sitemap'></i>
          </template>
          Schéma
        </vs-sidebar-item>
        <vs-sidebar-item id="domainExportImport" tag="router-link"
          :to="{ path: '/domain/'+  domain.domainName+'/exportimport' }">
          <template #icon>
            <i class='bx bx-export'></i>
          </template>
          LDIF Export & import
        </vs-sidebar-item>

      </vs-sidebar-group>
      <vs-sidebar-item id="server" tag="router-link" :to="{ path: '/server' }">
        <template #icon>
          <i class='bx bx-server'></i>
        </template>
        Server
      </vs-sidebar-item>
      <template #footer>
        <UserShow />
      </template>
    </vs-sidebar>
    <vs-navbar relative color="transparent">
      <template #right>
        <vs-switch v-model="darkmode" @click="checkDarkmode">
          <template #circle>
            <i v-if="!darkmode" class='bx bxs-moon'></i>
            <i v-else class='bx bxs-sun'></i>
          </template>
        </vs-switch>&nbsp;&nbsp;
        <vs-switch v-model="sidebarReduced" @click="checkSideRed">
          <template #circle>
            <i v-if="!sidebarReduced" class='bx bx-collapse'></i>
            <i v-else class='bx bx-expand'></i>
          </template>
        </vs-switch>
      </template>
    </vs-navbar>
  </div>

</template>

<script>
  import UserShow from '@/components/UserShow.vue';
  export default {
    name: 'Navbar',
    components: {
      UserShow
    },
    props: {
      domain: Object,
      isDomain: Boolean,
      active: String,
      sidebarReduced: Boolean
    },
    data() {
      return {
        darkmode: true
      };
    },
    methods: {
      checkDarkmode() {
        if (this.darkmode) {
          this.$vs.setTheme("dark");
        } else {
          this.$vs.setTheme("light");
        }
      },
      checkSideRed() {
        this.$parent.checkSideRed()
      }
    }
  }
</script>