<template>
    <div class="container is-fluid">
        
        <div style="display:flex; align-items:center">
            <vs-avatar @click="$router.go(-1)">
                <i class='bx bx-arrow-back'></i>
            </vs-avatar>
            <vs-avatar primary @click="$router.push({ path: '/domain/'+domainName.domainName+'/new_group' })" v-if="enabled" style="margin-left:15px; cursor:pointer">
                <i class='bx bx-plus'></i>
            </vs-avatar>
            <vs-avatar dark style="margin-left:15px">
                <i class='bx bx-group'></i>
            </vs-avatar>
            <h1 style="margin-left:15px">Skupiny</h1>
            
        </div>
        <div v-if="!enabled" class="columns is-centered">
            <img src="https://cdn.vespotok.net/img/domains-blocked.png">
        </div>
        <vs-table striped ref="content" v-model="selected" v-if="enabled">
            <template #header>
              <vs-input v-model="search" border placeholder="Search" />
            </template>
            <template #thead>
                <vs-tr>
                    <vs-th style="width: 0px">

                    </vs-th>
                    <vs-th>
                        Jméno
                    </vs-th>
                     <vs-th>
                        Popis
                    </vs-th>
                    <vs-th>
                        Obor názvů
                    </vs-th>
                </vs-tr>
            </template>
            <template #tbody>
                <vs-tr :key="tr" :is-selected="!!selected.includes(tr)" v-for="(group, tr) in $vs.getPage($vs.getSearch(grouplist, search), page, max)" :data="group">
                    <vs-td>
                        <div class="center">
                            <vs-button-group>
                                <vs-button icon color="primary" flat tag="router-link"
                                    :to="{ path: '/domain/'+domainName.domainName+'/group/'+group[1].dn }">
                                    <i class='bx bx-pencil'></i>
                                </vs-button>
                                <vs-button icon color="danger" flat tag="router-link"
                                    :to="{ path: '/domain/'+domainName.domainName+'/group_d/'+group[1].dn }">
                                    <i class='bx bx-x'></i>
                                </vs-button>
                            </vs-button-group>
                        </div>
                    </vs-td>
                    <vs-td>
                        {{ group[1].cn }}
                    </vs-td>
                    <vs-td>
                        {{ group[1].comment }}
                    </vs-td>
                    <vs-td>
                        {{ group[1].dn }}
                    </vs-td>
                </vs-tr>
            </template>
            <template #footer>
              <vs-pagination v-model="page" :length="$vs.getLength($vs.getSearch(grouplist, search), max)" />
            </template>
            <template #notFound>
              V této doméně se nenacházejí žádné skupiny. Stisknutím tlačítka&nbsp;&nbsp;<span style="background-color: #4a83ed; color: white; border-radius:5px; padding:5px; padding-left:7px; padding-right:7px"><i class='bx bx-plus'></i></span>&nbsp;&nbsp;přidáte první skupiny.
            </template>

        </vs-table>
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
          isEmpty: false,
          isLoading: false,
          grouplist: [
          ],
                selected: [],
                userAction: false,
                activeUser: {},
                page: 1,
                search: '',
                max: 10,
                active: 0,
                editActive: false,
                edit: null,
                editProp: {},
                allCheck: false,
                enabled: true
      };
  },
  props: {
      domainName: Object,
    },
  methods: {
          async loadgroups() {
            const hostname = this.$g('server_url')+"/v1/groups?domain="+this.domainName.domainName+"&auth="+this.$store.state.token;
            const results = await axios.get(hostname);
            const loading = this.$vs.loading({
                    color: 'primary',
                    type: 'corners'
                })
            await new Promise((resolve) => setTimeout(resolve, 500));
            if (results.data.status === "success") {
                this.isEmpty = false;
                this.grouplist = Object.entries(results.data.data);

            } else {
                    this.openNotification('top-center', 'danger',
                        `<i class='bx bx-select-multiple' ></i>`, 'K tomuto zdroji nemáte přístup!', 'Upozornění zabezpečení FJDCS');
                    this.enabled = false

                }
                this.$Progress.finish();
                this.isLoading = false;
                loading.close();
            },
            openNotification(position = null, color, icon, text, title) {
                this.$vs.notification({
                    icon,progress: 'auto',
                    color,
                    position,
                    title,
                    text
                })
            }
    },
    created(){
        this.loadgroups();
        this.$parent.activePage = "domainGroups";
    },
}
</script>