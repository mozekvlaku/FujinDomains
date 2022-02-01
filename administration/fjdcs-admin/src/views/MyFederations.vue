<template>
    <div class="container is-fluid">
        <div style="display:flex; align-items:center">
            <vs-avatar @click="$router.go(-1)">
                <i class='bx bx-arrow-back'></i>
            </vs-avatar>
            <vs-avatar dark style="margin-left:15px">
                <i class='bx bx-shape-triangle'></i>
            </vs-avatar>
            <h1 style="margin-left:15px">Mé federace</h1>
            
        </div>
       <div v-if="!haveFederations" class="columns is-centered">
            <img src="https://cdn.vespotok.net/img/domains-nofederations.png">
        </div>
        
        </div>
        
</template>
<script>
import axios from "axios";
export default {
  name: 'Domains',
  components: {
  },
  data() {
      return {
          loaded:false,
          domain: {

          },
          haveFederations: false
      };
  },
  props: {
      domainName: Object
    },
  methods: {
          async loadDomain() {
              const loading = this.$vs.loading({
                  color: 'primary',
                  type: 'corners'
                })
            const hostname = this.$g('server_url')+"/v1/domainlist";
            const results = await axios.get(hostname);
            await new Promise((resolve) => setTimeout(resolve, 500));
            if (results.data.status === "success") {
                this.domain = results.data.domains[this.domainName.domainName];
                this.loaded = true;
                this.$parent.activeDomain = this.domain;
                this.$parent.searchingDomain = true;
                this.$parent.activePage = "domainLink";
                loading.close();
            }
            this.$Progress.finish();
                this.isLoading = false;
             
          }
    },
    created(){
        this.$parent.activePage = "federations";
        //this.loadDomain();
    },
}
</script>