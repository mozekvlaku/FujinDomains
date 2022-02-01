<template>
    <div class="container is-fluid">

        <div style="display:flex; align-items:center">
            <vs-avatar @click="$router.go(-1)">
                <i class='bx bx-arrow-back'></i>
            </vs-avatar>
            <vs-avatar primary @click="$router.push({ path: '/domain/'+domainName.domainName+'/new_user' })" v-if="enabled" style="margin-left:15px; cursor:pointer">
                <i class='bx bx-plus'></i>
            </vs-avatar>
            <vs-avatar dark style="margin-left:15px">
                <i class='bx bx-user'></i>
            </vs-avatar>
            <h1 style="margin-left:15px">Uživatelé</h1>

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
                    <vs-th style="width: 0px">

                    </vs-th>
                    <vs-th>
                        Jméno a příjmení
                    </vs-th>
                    <vs-th>
                        Telefonní číslo
                    </vs-th>
                    <vs-th>
                        Email
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
                <vs-tr :key="tr" :is-selected="!!selected.includes(tr)"
                    v-for="(user, tr) in $vs.getPage($vs.getSearch(userlist, search), page, max)" :data="user">
                    <vs-td>
                        <div class="center">
                            <vs-button-group>
                                <vs-button icon color="primary" flat tag="router-link"
                                    :to="{ path: '/domain/'+domainName.domainName+'/user/'+user[1].dn }">
                                    <i class='bx bx-pencil'></i>
                                </vs-button>
                                <vs-button icon color="danger" flat tag="router-link"
                                    :to="{ path: '/domain/'+domainName.domainName+'/user_d/'+user[1].dn }">
                                    <i class='bx bx-x'></i>
                                </vs-button>
                            </vs-button-group>
                        </div>
                    </vs-td>
                    <vs-td width="90">
                        <vs-avatar v-if="user[1].thumbnailPhoto != undefined">
                            <img :src="user[1].thumbnailPhoto" />
                        </vs-avatar>
                        <vs-avatar v-if="user[1].thumbnailPhoto == undefined">
                            <template #text>
                                {{user[1].cn}}
                            </template>
                        </vs-avatar>

                    </vs-td>
                    <vs-td>
                        {{ user[1].cn }}
                    </vs-td>
                    <vs-td>
                        {{ user[1].telephoneNumber }}
                    </vs-td>
                    <vs-td>
                        {{ user[1].mail }}
                    </vs-td>
                    <vs-td>
                        {{ user[1].comment }}
                    </vs-td>
                    <vs-td>
                        {{ user[1].dn }}
                    </vs-td>
                </vs-tr>
            </template>
            <template #footer>
                <vs-pagination v-model="page" :length="$vs.getLength($vs.getSearch(userlist, search), max)" />
            </template>
            <template #notFound>
                V této doméně se nenacházejí žádní uživatelé. Stisknutím tlačítka&nbsp;&nbsp;<span
                    style="background-color: #4a83ed; color: white; border-radius:5px; padding:5px; padding-left:7px; padding-right:7px"><i
                        class='bx bx-plus'></i></span>&nbsp;&nbsp;přidáte prvního uživatele.
            </template>

        </vs-table>

        <vs-dialog autowidth blur v-model="userAction">
            <template #header>
                <h4 class="x">
                    Přidávání uživatele do domény
                </h4>
            </template>


            <div class="con-content">
                <div class="center content-inputs">
                    <vs-input v-model="value1" placeholder="User name">
                        <template #icon>
                            <i class='bx bx-user'></i>
                        </template>
                    </vs-input>

                    <vs-input type="password" icon-after v-model="value2" placeholder="Password">
                        <template #icon>
                            <i class='bx bx-lock-open-alt'></i>
                        </template>
                    </vs-input>
                </div>
            </div>

            <template #footer>
                <div class="con-footer">
                    <vs-button @click="userAction=false" transparent>
                        Přidat
                    </vs-button>
                    <vs-button @click="userAction=false" dark transparent>
                        Zrušit
                    </vs-button>
                </div>
            </template>
        </vs-dialog>
    </div>
</template>
<script>
    import axios from "axios";
    export default {
        name: 'Domains',
        components: {},
        data() {
            return {
                isEmpty: true,
                isLoading: true,
                userlist: [],
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
                const loading = this.$vs.loading({
                    color: 'primary',
                    type: 'corners'
                })
                const hostname = this.$g('server_url') + "/v1/users?domain=" + this.domainName.domainName +
                    "&auth=" + this.$store.state.token;
                const results = await axios.get(hostname);
                await new Promise((resolve) => setTimeout(resolve, 500));
                if (results.data.status === "success") {
                    this.isEmpty = false;
                    this.userlist = Object.entries(results.data.data);
                    console.log(this.userlist);
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

        created() {
            this.loadUsers();
            this.$parent.activePage = "domainUsers";
        },
    }
</script>