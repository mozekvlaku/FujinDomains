<template>
    <b-navbar>
        
        <template #brand>
            <b-navbar-item tag="router-link" :to="{ path: '/' }">
                <img
                    src="https://cdn.vespotok.net/img/fujindomainshorizontal.svg"
                    alt="Logo"
                    style="height: 100%;width:160px;margin-top:3px"
                >
            </b-navbar-item>
        </template>
        <template #start>
            <b-navbar-item tag="div">
              Spravujete&nbsp;<b>{{domainName}}</b>
            </b-navbar-item>
            <b-navbar-item tag="router-link" :to="{ path: '/domain/'+domainName+'/users' }">
               <font-awesome-icon class="has-text-primary" icon="user-friends" />&nbsp;Uživatelé
            </b-navbar-item>
            <b-navbar-item tag="router-link" :to="{ path: '/domain/'+domainName+'/schema' }">
               <font-awesome-icon class="has-text-primary" icon="project-diagram" />&nbsp;Schéma
            </b-navbar-item>
            <b-navbar-item tag="router-link" :to="{ path: '/domain/'+domainName+'/groups' }">
               <font-awesome-icon class="has-text-primary" icon="users" />&nbsp;Skupiny
            </b-navbar-item>
            <b-navbar-item tag="router-link" :to="{ path: '/domain/'+domainName+'/exportimport' }">
               <font-awesome-icon class="has-text-primary" icon="file" />&nbsp;Export & import
            </b-navbar-item>
            <b-navbar-item tag="router-link" :to="{ path: '/domain/'+domainName }">
               <font-awesome-icon class="has-text-primary" icon="cog" />&nbsp;Nastavení domény
            </b-navbar-item>
            
        </template>

        <template #end>
                <UserShow/>
        </template>
        
    </b-navbar>
</template>
<script>
import UserShow from '@/components/UserShow.vue';
export default {
  name: 'DomainNavbar',
  components: {
      UserShow
  }
  ,props: {
      domainName: String
    },
}
</script>