<template>
    <div>

        <div style="display:flex; align-items:center">
            <vs-avatar @click="$router.go(-1)">
                <i class='bx bx-arrow-back'></i>
            </vs-avatar>
            <vs-avatar primary @click="handleSubmit" style="margin-left:15px; cursor:pointer">
                <i class='bx bx-save'></i>
            </vs-avatar>
            <vs-avatar primary @click="adding=true" style="margin-left:15px; cursor:pointer">
                <i class='bx bx-plus'></i>
            </vs-avatar>
            <vs-avatar danger @click="deleting=!deleting" style="margin-left:15px; cursor:pointer">
                <i class='bx bx-x'></i>
            </vs-avatar>
            <vs-avatar dark style="margin-left:15px">
                <i class='bx bx-group'></i>
            </vs-avatar>
            <h1 style="margin-left:15px">{{currentFormData.cn}}
            </h1>

        </div>
        <br>

        <div class="container is-fluid">

            <h3 class="is-marginless is-paddingless">Obecné</h3>
            <br>
            <div class="columns is-multiline">
                <div class="column is-narrow">
                    <vs-input type="text" @input="handleChange('cn')" v-model="currentFormData.cn"
                        label="Název skupiny" />
                </div>
                <div class="column is-narrow">
                    <vs-input type="text" @input="handleChange('comment')" v-model="currentFormData.comment"
                        label="Popis" />
                </div>
                <div class="column is-narrow">
                    <vs-input type="text" @input="handleChange('mail')" v-model="currentFormData.mail" label="Email">
                        <template #icon>
                            <i class='bx bx-mail-send'></i>
                        </template></vs-input>
                </div>
                <div class="column is-narrow is-hidden">
                    <vs-switch dark>
                        <template #off>
                            Zabezpečovací skupina
                        </template>
                        <template #on>
                            Distribuční skupina
                        </template>
                    </vs-switch>
                </div>
            </div>
            <h3 class="is-marginless is-paddingless">Členové</h3>
            <br>
            <vs-table striped ref="content">
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
                    <vs-tr :key="tr" v-for="(user, tr) in member" :data="user">
                        <vs-td>
                            <div class="center">
                                <vs-button icon color="danger" flat @click="removing=true;removingObject=user[1].dn">
                                    <i class='bx bx-folder-minus'></i>
                                </vs-button>
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

                <template #notFound>
                    V této skupině se nenacházejí žádní uživatelé ani jiné objekty. Stisknutím tlačítka&nbsp;&nbsp;<span
                        style="background-color: #4a83ed; color: white; border-radius:5px; padding:5px; padding-left:7px; padding-right:7px"><i
                            class='bx bx-plus'></i></span>&nbsp;&nbsp;přidáte prvního uživatele.
                </template>

            </vs-table>
        </div>
        <vs-dialog width="550px" v-model="deleting">
            <template #header>
                <h4>
                    Připravujete se smazat skupinu {{currentFormData.cn}}!
                </h4>
            </template>


            <div class="con-content">
                <p>
                    Kliknutím na tlačítko Smazat smažete skupinu {{currentFormData.cn}}! Jste si
                    naprosto jisti, že chcete skupinu smazat? TATO AKCE SE NEDÁ ODVOLAT!
                </p>
            </div>

            <template #footer>
                <div class="con-footer">
                    <div class="columns is-centered is-mobile">
                        <div class="column is-narrow">
                            <vs-button @click="deleteMe" danger>
                                Smazat
                            </vs-button>
                        </div>
                        <div class="column is-narrow">
                            <vs-button @click="deleting=false" dark>
                                Storno
                            </vs-button>
                        </div>
                    </div>
                </div>
            </template>
        </vs-dialog>
        <vs-dialog width="550px" v-model="removing">
            <template #header>
                <h4>
                    Chcete odstranit objekt ze skupiny?
                </h4>
            </template>


            <div class="con-content">
                <p>
                    Kliknutím na tlačítko Odstranit odeberete objekt ze skupiny {{currentFormData.cn}}. Pokud se
                    rozhodnete objekt zpět přidat, klikněte na tlačítko přidat.
                </p>
            </div>

            <template #footer>
                <div class="con-footer">
                    <div class="columns is-centered is-mobile">
                        <div class="column is-narrow">
                            <vs-button @click="removeFromGroup" danger>
                                Odstranit
                            </vs-button>
                        </div>
                        <div class="column is-narrow">
                            <vs-button @click="removing=false" dark>
                                Storno
                            </vs-button>
                        </div>
                    </div>
                </div>
            </template>
        </vs-dialog>
        <vs-dialog v-model="adding">
            <template #header>
                <h4 class="is-marginless">
                    <br>
                    Najít - Uživatelé, kontakty a skupiny
                </h4>
            </template>


            <div class="con-form">

                <div class="columns is-centered is-vcentered is-mobile">
                    <div class="column is-narrow">
                        <vs-input v-model="search.query" placeholder="Název">
                            <template #icon>
                                >
                            </template>
                        </vs-input>
                    </div>
                    <div class="column is-narrow">
                        <vs-button flat icon @click="searchInDirectory">
                            <i class='bx bx-search'></i> &nbsp;
                            Najít
                        </vs-button>
                    </div>
                </div>
            </div>

            <template #footer>
<vs-table striped ref="content">
                <template #thead>
                    <vs-tr>
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
                    </vs-tr>
                </template>
                <template #tbody>
                    <vs-tr :key="tr" v-for="(result, tr) in search.results" :data="result">
                        <vs-td>
                            <div class="center">
                                <vs-button icon color="primary" flat @click="addingObject=result[1].dn;addToGroup()">
                                    <i class='bx bx-select-multiple'></i>
                                </vs-button>
                            </div>
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
                    </vs-tr>
                </template>

                <template #notFound>
                    Vyhledávejte zadáním jména, či oboru názvů hledaného subjektu
                </template>

            </vs-table>
            </template>
        </vs-dialog>
    </div>
</template>
<script>
    import axios from "axios";
    export default {
        name: 'Domains',
        data() {
            return {
                group: {

                },
                changedFields: [],
                currentFormData: null,
                picture: false,
                member: [],
                removing: false,
                removingObject: "",
                addingObject: "",
                adding: false,
                search: {
                    query: "",
                    results: []
                }
            };
        },
        props: {
            domainName: Object,
            deleting: Boolean
        },
        computed: {
            changedFormData() {
                return this.changedFields.reduce((formdata, field) => {
                    formdata[field] = this.currentFormData[field];

                    return formdata;
                }, {});
            },
        },
        methods: {
            async getObjects(sids) {
                const loading = this.$vs.loading({
                    color: 'primary',
                    type: 'corners'
                })
                const hostname = this.$g('server_url') + "/v1/sids?q=" + sids +
                    "&domain=" + this.domainName.domainName.domainName +
                    "&auth=" + this.$store.state.token;

                const results = await axios.get(hostname);
                await new Promise((resolve) => setTimeout(resolve, 500));
                if (results.data.status === "success") {
                    //this.group = results.data.data[this.domainName.domainName.userDn];
                    this.member = Object.entries(results.data.data);
                    console.log(this.member);
                }
                if (results.data === 401) {
                    this.openNotification('top-center', 'danger',
                        `<i class='bx bx-select-multiple' ></i>`, 'K tomuto zdroji nemáte přístup!',
                        'Upozornění zabezpečení FJDCS');
                }
                this.$Progress.finish();
                loading.close();
            },
            async loadGroup() {
                const loading = this.$vs.loading({
                    color: 'primary',
                    type: 'corners'
                })
                const hostname = this.$g('server_url') + "/v1/search?q=" + this.domainName.domainName.groupDn +
                    "&domain=" + this.domainName.domainName.domainName +
                    "&auth=" + this.$store.state.token;

                const results = await axios.get(hostname);
                await new Promise((resolve) => setTimeout(resolve, 500));
                if (results.data.status === "success") {
                    //this.group = results.data.data[this.domainName.domainName.userDn];
                    this.currentFormData = results.data.data[this.domainName.domainName.groupDn];
                    this.getObjects(this.currentFormData.member);
                }
                if (results.data === 401) {
                    this.openNotification('top-center', 'danger',
                        `<i class='bx bx-select-multiple' ></i>`, 'K tomuto zdroji nemáte přístup!',
                        'Upozornění zabezpečení FJDCS');
                }
                this.$Progress.finish();
                loading.close();
            },
            async searchInDirectory() {
                const loading = this.$vs.loading({
                    color: 'primary',
                    type: 'corners'
                })
                const hostname = this.$g('server_url') + "/v1/search?q=" + this.search.query +
                    "&domain=" + this.domainName.domainName.domainName +
                    "&auth=" + this.$store.state.token;

                const results = await axios.get(hostname);
                await new Promise((resolve) => setTimeout(resolve, 500));
                if (results.data.status === "success") {
                    this.search.results = Object.entries(results.data.data);
                }
                if (results.data === 401) {
                    this.openNotification('top-center', 'danger',
                        `<i class='bx bx-select-multiple' ></i>`, 'K tomuto zdroji nemáte přístup!',
                        'Upozornění zabezpečení FJDCS');
                }
                this.$Progress.finish();
                loading.close();
            },
            async deleteMe() {
                const loading = this.$vs.loading({
                    color: 'success',
                    type: 'corners'
                })


                var data = new FormData();
                data.append('dn', this.domainName.domainName.groupDn);
                data.append('domain', this.domainName.domainName.domainName);
                data.append('auth', this.$store.state.token);
                const hostname = this.$g('server_url') + "/v1/delete";
                axios.post(hostname, data, {
                        headers: {
                            'Content-Type': 'application/json'
                        }
                    })
                    .then((response) => {
                        if (response.data.status === "success") {
                            this.openNotification('top-center', 'success',
                                `<i class='bx bx-select-multiple' ></i>`, 'Skupina byla úspěšně smazána',
                                'Operace úspěšná');
                            this.$router.go(-1);
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
                    }).catch(function (error) {
                        this.openNotification('top-center', 'danger',
                            `<i class='bx bx-select-multiple' ></i>`, error, 'Chyba systému');
                    });

            },
            async addToGroup() {
                const loading = this.$vs.loading({
                    color: 'success',
                    type: 'corners'
                })


                var data = new FormData();
                data.append('dn', this.domainName.domainName.groupDn);
                data.append('dnToAdd', this.addingObject);
                data.append('domain', this.domainName.domainName.domainName);
                data.append('auth', this.$store.state.token);
                const hostname = this.$g('server_url') + "/v1/addTo";
                axios.post(hostname, data, {
                        headers: {
                            'Content-Type': 'application/json'
                        }
                    })
                    .then((response) => {
                        if (response.data.status === "success") {
                            this.openNotification('top-center', 'success',
                                `<i class='bx bx-select-multiple' ></i>`,
                                'Objekt byl úspěšně přidán do skupiny',
                                'Operace úspěšná');
                            this.adding = false;
                            this.loadGroup();
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
                    }).catch(function (error) {
                        this.openNotification('top-center', 'danger',
                            `<i class='bx bx-select-multiple' ></i>`, error, 'Chyba systému');
                    });

            },
            async removeFromGroup() {
                const loading = this.$vs.loading({
                    color: 'success',
                    type: 'corners'
                })


                var data = new FormData();
                data.append('dn', this.domainName.domainName.groupDn);
                data.append('dnToRemove', this.removingObject);
                data.append('domain', this.domainName.domainName.domainName);
                data.append('auth', this.$store.state.token);
                const hostname = this.$g('server_url') + "/v1/removeFrom";
                axios.post(hostname, data, {
                        headers: {
                            'Content-Type': 'application/json'
                        }
                    })
                    .then((response) => {
                        if (response.data.status === "success") {
                            this.openNotification('top-center', 'success',
                                `<i class='bx bx-select-multiple' ></i>`,
                                'Objekt byl úspěšně odebrán ze skupiny',
                                'Operace úspěšná');
                            this.removing = false;
                            this.loadGroup();
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
                    }).catch(function (error) {
                        this.openNotification('top-center', 'danger',
                            `<i class='bx bx-select-multiple' ></i>`, error, 'Chyba systému');
                    });

            },
            async changeFields() {
                const loading = this.$vs.loading({
                    color: 'success',
                    type: 'corners'
                })


                var data = new FormData();
                data.append('dn', this.domainName.domainName.groupDn);
                data.append('domain', this.domainName.domainName.domainName);
                data.append('auth', this.$store.state.token);
                data.append('data', JSON.stringify(this.changedFormData));
                const hostname = this.$g('server_url') + "/v1/change";
                axios.post(hostname, data, {
                        headers: {
                            'Content-Type': 'application/json'
                        }
                    })
                    .then((response) => {
                        if (response.data.status === "success") {
                            this.openNotification('top-center', 'success',
                                `<i class='bx bx-select-multiple' ></i>`,
                                'Uživatel byl úspěšně aktualizován', 'Operace úspěšná');
                            this.$router.go(-1);

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
                    }).catch(function (error) {
                        this.openNotification('top-center', 'danger',
                            `<i class='bx bx-select-multiple' ></i>`, error, 'Chyba systému');
                    });

            },
            openNotification(position = null, color, icon, text, title) {
                this.$vs.notification({
                    icon,
                    progress: 'auto',
                    color,
                    position,
                    title,
                    text
                })
            },
            handleSubmit() {
                if (this.changedFields.length === 0) {
                    this.$router.go(-1);
                    return;
                }

                this.changeFields();

            },

            handleChange(changedField) {
                const results = this.changedFields.filter((field) => field !== changedField);
                if (this.group[changedField] !== this.currentFormData[changedField]) {
                    results.push(changedField);
                }

                this.changedFields = results;
            },
        },
        created() {
            this.loadGroup();
            this.$parent.activePage = "domainUser";
            console.log(this.$props)
        },
    }
</script>