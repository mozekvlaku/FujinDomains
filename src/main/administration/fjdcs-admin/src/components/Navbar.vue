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
            <b-navbar-item tag="router-link" :to="{ path: '/domain-list' }">
               Seznam domén
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
  name: 'Navbar',
  components: {
      UserShow
  }
}
</script>