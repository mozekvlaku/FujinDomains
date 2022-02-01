<template>
    <div class="container is-fluid">
<div style="display:flex; align-items:center">
            <vs-avatar @click="$router.go(-1)">
                <i class='bx bx-arrow-back'></i>
            </vs-avatar>
            <vs-avatar primary @click="$router.push({ path: '/new_domain' })" style="margin-left:15px; cursor:pointer">
                <i class='bx bx-plus'></i>
            </vs-avatar>
            <vs-avatar dark style="margin-left:15px">
                <i class='bx bx-grid-alt'></i>
            </vs-avatar>
            <h1 style="margin-left:15px">Domény</h1>
            
        </div>
        
        <vs-table striped ref="content" v-if="$store.state.userlevel == 'SERVER'">
            <template #thead>
                <vs-tr>
                    <vs-th style="width: 0px">

                    </vs-th>
                    <vs-th>
                        Logo
                    </vs-th>
                    <vs-th>
                        Organizace
                    </vs-th>
                    <vs-th>
                        Doména
                    </vs-th>
                    <vs-th>
                        NT4 doménové jméno
                    </vs-th>
                    <vs-th>
                        Obor názvů
                    </vs-th>
                </vs-tr>
            </template>
            <template #tbody>
                <vs-tr :key="i" v-for="(domain, i) in domainlist" :data="domain">
                    <vs-td>
                        
                        <div class="center">
                            <vs-button-group v-if="domain.domainName!='builtin.local'">
                                <vs-button icon color="primary" flat tag="router-link"
                                    :to="{ path: '/domain/'+domain.domainName }">
                                    <i class='bx bx-window-open'></i>
                                </vs-button>
                                <vs-button icon v-if="domain.domainName!='builtin.local'" color="danger" flat @click="removingDomain=true;remDomNam=domain.domainName;">
                                    <i class='bx bx-x'></i>
                                </vs-button>
                            </vs-button-group>
                            <vs-button icon color="primary" v-if="domain.domainName=='builtin.local'"  flat tag="router-link"
                                    :to="{ path: '/domain/'+domain.domainName }">
                                    <i class='bx bx-window-open'></i>
                                </vs-button>
                        </div>
                    </vs-td>
                    <vs-td width="90">
                        <img :src="domain.organizationLogo " style="width:90px" />
                    </vs-td>
                    <vs-td>
                        {{ domain.organizationName }}
                    </vs-td>
                    <vs-td>
                        {{ domain.domainName }}
                    </vs-td>
                    <vs-td>
                        {{ domain.samDomainName }}
                    </vs-td>
                    <vs-td>
                        {{ domain.dn }}
                    </vs-td>
                </vs-tr>
            </template>
        </vs-table>
        <vs-dialog width="550px" v-model="removingDomain">
            <template #header>
                <h4>
                    Připravujete se vymazat doménu {{remDomNam}}!!!
                </h4>
            </template>


            <div class="con-content">
                <p>
                    TATO AKCE JE NEODVRATNÁ A SMAŽE VEŠKERÉ UŽIVATELSKÉ ÚČTY, CELÉ SCHÉMA, SKUPINY A NAPROSTO VŠECHNY ATRIBUTY ADRESÁŘOVÝCH OBJEKTŮ TÉTO DOMÉNY!
                    Jste si na 100% jisti, že chcete tuto doménu smazat? Ztracené údaje již nebude moci obnovit!
                </p>
            </div>

            <template #footer>
                <div class="con-footer">
                    <div class="columns is-centered is-mobile">
                        <div class="column is-narrow">
                            <vs-button @click="certainRemovingDomain=true;removingDomain=false" danger>
                                Smazat
                            </vs-button>
                        </div>
                        <div class="column is-narrow">
                            <vs-button @click="removingDomain=false" dark>
                                Storno
                            </vs-button>
                        </div>
                    </div>
                </div>
            </template>
        </vs-dialog>

        <vs-dialog width="550px" v-model="certainRemovingDomain">
            <template #header>
                <h4>
                    Jste si opravdu jisti?
                </h4>
            </template>


            <div class="con-content">
                <p>
                    <blink><b>TATO AKCE JE NEODVRATNÁ A SMAŽE VEŠKERÉ UŽIVATELSKÉ ÚČTY, CELÉ SCHÉMA, SKUPINY A NAPROSTO VŠECHNY ATRIBUTY ADRESÁŘOVÝCH OBJEKTŮ TÉTO DOMÉNY!</b></blink>
                    Jste si na 100% jisti, že chcete tuto doménu smazat? Ztracené údaje již nebude moci obnovit!
                </p>
            </div>

            <template #footer>
                <div class="con-footer">
                    <div class="columns is-centered is-mobile">
                        <div class="column is-narrow">
                            <vs-button @click="deleteDomain" danger>
                                Opravdu chci doménu smazat
                            </vs-button>
                        </div>
                        <div class="column is-narrow">
                            <vs-button @click="certainRemovingDomain=false" dark>
                                Storno
                            </vs-button>
                        </div>
                    </div>
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
                domainlist: [],
                removingDomain: false,
                certainRemovingDomain: false,
                remDomNam: ""
            };
        },
        methods: {
            async loadDomains() {
                const loading = this.$vs.loading({
                  color: 'primary',
                  type: 'corners'
                })
                const hostname = this.$g('server_url') + "/v1/domainlist";
                const results = await axios.get(hostname);
                await new Promise((resolve) => setTimeout(resolve, 500));
                if (results.data.status === "success") {
                    this.isEmpty = false;
                    this.domainlist = results.data.domains;
                }
                this.$Progress.finish();
                this.isLoading = false;
                loading.close();

            },async deleteDomain() {
                const loading = this.$vs.loading({
                    color: 'success',
                    type: 'corners'
                })


                var data = new FormData();
                data.append('domain', this.remDomNam);
                data.append('auth', this.$store.state.token);
                const hostname = this.$g('server_url') + "/v1/removedomain";
                axios.post(hostname, data, {
                        headers: {
                            'Content-Type': 'application/json'
                        }
                    })
                    .then((response) => {
                        if (response.data.status === "success") {
                            this.openNotification('top-center', 'success',
                                `<i class='bx bx-select-multiple' ></i>`, 'Doména byla úspěšně smazána',
                                'Operace úspěšná');
                            loading.close();
                            this.loadDomains();
                            this.certainRemovingDomain=false;
                            
                        } else {
                            this.openNotification('top-center', 'danger',
                                `<i class='bx bx-select-multiple' ></i>`, response.data.message,
                                'Upozornění zabezpečení FJDCS');
                        }
                        if (response.data === 401) {
                            this.openNotification('top-center', 'danger',
                                `<i class='bx bx-select-multiple' ></i>`, 'K tomuto zdroji nemáte přístup!',
                                'Upozornění zabezpečení FJDCS');
                        }
                        loading.close();
                    }).catch((error) => {
                        loading.close();
                        this.loadDomains();
                        this.certainRemovingDomain=false;

                        this.openNotification('top-center', 'danger',
                            `<i class='bx bx-select-multiple' ></i>`, error, 'Chyba systému');
                    });

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
            this.loadDomains();
            this.$parent.searchingDomain = false;
            this.$parent.activePage = "/domain-list";
        },
    }
</script>