<template>
    <div class="container is-fluid">

        <div style="display:flex; align-items:center">
            <vs-avatar @click="$router.go(-1)">
                <i class='bx bx-arrow-back'></i>
            </vs-avatar>
            <vs-avatar dark style="margin-left:15px">
                <i class='bx bx-search'></i>
            </vs-avatar>
            <h1 style="margin-left:15px">Výsledky hledání</h1>

        </div>
        <vs-table striped>
            <template #thead>
                <vs-tr>
                    <vs-th style="width: 0px">

                    </vs-th>
                    <vs-th style="width: 0px">

                    </vs-th>
                    <vs-th>
                        Název
                    </vs-th>
                    <vs-th>
                        Typ
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
                <vs-tr :key="tr" v-for="(result, tr) in results" :data="result">
                    <vs-td>
                        <div class="center">
                                <vs-button icon color="primary" flat tag="router-link" v-if="result[1].objectClass == 'person'"
                                    :to="{ path: '/domain/'+domainName.domainName.domainName+'/user/'+result[1].dn }">
                                    <i class='bx bx-pencil'></i>
                                </vs-button>
                                <vs-button icon color="primary" flat tag="router-link" v-if="result[1].objectClass == 'group'"
                                    :to="{ path: '/domain/'+domainName.domainName.domainName+'/group/'+result[1].dn }">
                                    <i class='bx bx-pencil'></i>
                                </vs-button>
                        </div>
                    </vs-td>
                    <vs-td width="90">
                        <vs-avatar  v-if="result[1].objectClass == 'person'">
                            <i class='bx bx-user'></i>
                        </vs-avatar>
                        <vs-avatar  v-if="result[1].objectClass == 'group'">
                            <i class='bx bx-group'></i>
                        </vs-avatar>
                        <vs-avatar  v-if="result[1].objectClass == 'container'">
                            <i class='bx bx-box'></i>
                        </vs-avatar>
                        <vs-avatar  v-if="result[1].objectClass == 'organizationalUnit'">
                            <i class='bx bx-baguette'></i>
                        </vs-avatar>

                    </vs-td>
                    <vs-td>
                        {{ result[1].cn }}
                    </vs-td>
                    <vs-td>
                        {{ result[1].objectClass }}
                    </vs-td>
                    <vs-td>
                        {{ result[1].comment }}
                    </vs-td>
                    <vs-td>
                        {{ result[1].dn }}
                    </vs-td>
                </vs-tr>
            </template>
            <template #notFound>
                Nebylo nic nalezeno, zkuste formulovat jinak. Hledat můžete podle jména, či atributu dn.
            </template>

        </vs-table>
    </div>
</template>
<script>
    import axios from "axios";
    export default {
        name: 'SearchResults',
        components: {},
        data() {
            return {
                results: [],
                selected: [],
                userAction: false,
                activeUser: {},
                page: 1,
                search: '',
                max: 20,
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
            async loadUsers() {
                const hostname = this.$g('server_url') + "/v1/search?domain=" + this.domainName.domainName.domainName +
                    "&auth=" + this.$store.state.token +
                    "&q=" + this.domainName.domainName.query;
                const results = await axios.get(hostname);
                this.results = [];
                await new Promise((resolve) => setTimeout(resolve, 500));
                if (results.data.status === "success") {
                    this.results = Object.entries(results.data.data);
                    
                } else {
                    
                    this.openNotification('top-center', 'danger',
                        `<i class='bx bx-select-multiple' ></i>`, 'K tomuto zdroji nemáte přístup!', 'Upozornění zabezpečení FJDCS');
                   
                }
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

        created() {
            this.loadUsers();
            this.$parent.activePage = "search";
        },
    }
</script>