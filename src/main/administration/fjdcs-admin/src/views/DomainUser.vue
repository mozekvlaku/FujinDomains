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
                <i class='bx bx-group'></i>
            </vs-avatar>
            <vs-avatar danger @click="deleting=true" style="margin-left:15px; cursor:pointer">
                <i class='bx bx-x'></i>
            </vs-avatar>
            <vs-avatar dark style="margin-left:15px">
                <i class='bx bx-user'></i>
            </vs-avatar>
            <h1 style="margin-left:15px">{{currentFormData.cn}} <small>({{currentFormData.userPrincipalName}})</small>
            </h1>

        </div>
        <br>

        <div class="columns">
            <div class="column is-narrow">
                <vs-avatar square size="320" style="font-size: 5rem;">
                    <img :src="currentFormData.thumbnailPhoto" v-if="picture">
                    <template #text v-if="!picture">
                        {{currentFormData.cn}}
                    </template>
                </vs-avatar><br>
                <br>
                <vs-input type="text" border style="width:100%" @input="handleChange('thumbnailPhoto')"
                    v-model="currentFormData.thumbnailPhoto" label="URL profilové fotky" />
            </div>
            <div class="column ">
                <h3 class="is-marginless is-paddingless">Obecné</h3>
                <br>
                <div class="columns is-multiline">
                    <div class="column is-narrow">
                        <vs-input type="text" @input="handleChange('givenName')" v-model="currentFormData.givenName"
                            label="Jméno" />
                    </div>
                    <div class="column is-narrow">
                        <vs-input type="text" @input="handleChange('sn')" v-model="currentFormData.sn"
                            label="Příjmení" />
                    </div>
                    <div class="column is-narrow">
                        <vs-input type="text" @input="handleChange('initials')" v-model="currentFormData.initials"
                            label="Iniciály dalších jmen" />
                    </div>
                    <div class="column is-narrow">
                        <vs-input type="text" @input="handleChange('cn')" v-model="currentFormData.cn"
                            label="Zobrazované jméno" />
                    </div>
                    <div class="column is-narrow">
                        <vs-input type="text" @input="handleChange('comment')" v-model="currentFormData.comment"
                            label="Popis" />
                    </div>
                    <div class="column is-narrow">
                        <vs-input type="text" @input="handleChange('physicalDeliveryOfficeName')"
                            v-model="currentFormData.physicalDeliveryOfficeName" label="Kancelář" />
                    </div>
                    <div class="column is-narrow">
                        <vs-input type="text" @input="handleChange('telephoneNumber')"
                            v-model="currentFormData.telephoneNumber" label="Telefonní číslo"><template #icon>
                                <i class='bx bx-phone'></i>
                            </template></vs-input>
                    </div>
                    <div class="column is-narrow">
                        <vs-input type="text" @input="handleChange('mail')" v-model="currentFormData.mail"
                            label="Email"><template #icon>
                                <i class='bx bx-mail-send'></i>
                            </template></vs-input>
                    </div>
                    <div class="column is-narrow">
                        <vs-input type="text" @input="handleChange('wwwHomePage')" v-model="currentFormData.wwwHomePage"
                            label="Webová stránka"><template #icon>
                                <i class='bx bxl-internet-explorer'></i>
                            </template></vs-input>
                    </div>
                </div>
                <h3 class="is-marginless is-paddingless">Adresa</h3>
                <br>
                <div class="columns is-multiline">
                    <div class="column is-narrow">
                        <vs-input type="text" @input="handleChange('streetAddress')"
                            v-model="currentFormData.streetAddress" label="Ulice" />
                    </div>
                    <div class="column is-narrow">
                        <vs-input type="text" @input="handleChange('postOfficeBox')"
                            v-model="currentFormData.postOfficeBox" label="Poštovní přihrádka" />
                    </div>
                    <div class="column is-narrow">
                        <vs-input type="text" @input="handleChange('l')" v-model="currentFormData.l" label="Město" />
                    </div>
                    <div class="column is-narrow">
                        <vs-input type="text" @input="handleChange('st')" v-model="currentFormData.st" label="Okres" />
                    </div>
                    <div class="column is-narrow">
                        <vs-input type="text" @input="handleChange('postalCode')" v-model="currentFormData.postalCode"
                            label="PSČ" />
                    </div>
                    <div class="column is-narrow">
                        <vs-input type="text" @input="handleChange('c')" v-model="currentFormData.c"
                            label="Země/oblast" />
                    </div>
                </div>
                <h3 class="is-marginless is-paddingless">Účet</h3>
                <br>
                <div class="columns is-multiline">
                    <div class="column is-narrow">
                        <vs-input type="text" @input="handleChange('userPrincipalName')"
                            v-model="currentFormData.userPrincipalName" label="Přihlašovací jméno"><template #icon>
                                <i class='bx bx-user'></i>
                            </template></vs-input>
                    </div>
                    <div class="column is-narrow">
                        <vs-input type="text" @input="handleChange('samAccountName')"
                            v-model="currentFormData.samAccountName" label="Přihlašovací jméno (NT4)"><template #icon>
                                <i class='bx bx-user'></i>
                            </template></vs-input>
                    </div>
                </div>
                <h3 class="is-marginless is-paddingless">Profil</h3>
                <br>
                <div class="columns is-multiline">
                    <div class="column is-narrow">
                        <vs-input type="text" @input="handleChange('fujinSh')" v-model="currentFormData.fujinSh"
                            label="Textový shell Fujinu"><template #icon>
                                <i class='bx bx-terminal'></i>
                            </template></vs-input>
                    </div>
                    <div class="column is-narrow">
                        <vs-input type="text" @input="handleChange('fujinGui')" v-model="currentFormData.fujinGui"
                            label="Grafický shell Fujin"><template #icon>
                                <i class='bx bx-mouse-alt'></i>
                            </template></vs-input>
                    </div>
                    <div class="column is-narrow">
                        <vs-input type="text" @input="handleChange('fujinHome')" v-model="currentFormData.fujinHome"
                            label="Domovská složka Fujinu"><template #icon>
                                <i class='bx bx-home'></i>
                            </template></vs-input>
                    </div>
                </div>
                <h3 class="is-marginless is-paddingless">Telefony</h3>
                <br>
                <div class="columns is-multiline">
                    <div class="column is-narrow">
                        <vs-input type="text" @input="handleChange('homePhone')" v-model="currentFormData.homePhone"
                            label="Domů" />
                    </div>
                    <div class="column is-narrow">
                        <vs-input type="text" @input="handleChange('otherTelephone')"
                            v-model="currentFormData.otherTelephone" label="Operátor" />
                    </div>
                    <div class="column is-narrow">
                        <vs-input type="text" @input="handleChange('mobile')" v-model="currentFormData.mobile"
                            label="Mobilní telefon" />
                    </div>
                    <div class="column is-narrow">
                        <vs-input type="text" @input="handleChange('fax')" v-model="currentFormData.fax" label="Fax" />
                    </div>
                    <div class="column is-narrow">
                        <vs-input type="text" @input="handleChange('ipPhone')" v-model="currentFormData.ipPhone"
                            label="Telefon IP" />
                    </div>
                </div>
                <h3 class="is-marginless is-paddingless">Je členem ve skupinách</h3>
                <br>
                <vs-table striped ref="content">
            <template #thead>
                <vs-tr>
                    <vs-th style="width: 0px">

                    </vs-th>
                    <vs-th style="width: 0px">

                    </vs-th>
                    <vs-th>
                        Jméno
                    </vs-th>
                    <vs-th>
                        Obor názvů
                    </vs-th>
                </vs-tr>
            </template>
            <template #tbody>
                <vs-tr :key="tr" v-for="(group, tr) in member" :data="group">
                    <vs-td>
                        <div class="center">
                                <vs-button icon color="danger" flat @click="removing=true;removingObject=group[1].dn">
                                    <i class='bx bx-folder-minus'></i>
                                </vs-button>
                        </div>
                    </vs-td>
                    <vs-td width="90">
                        <vs-avatar>
                            <i class='bx bx-group'></i>
                        </vs-avatar>

                    </vs-td>
                    <vs-td>
                        {{ group[1].cn }}
                    </vs-td>
                    <vs-td>
                        {{ group[1].dn }}
                    </vs-td>
                </vs-tr>
            </template>
           
            <template #notFound>
                {{ currentFormData.cn }} se nenachází v žádné skupině. Stisknutím tlačítka&nbsp;&nbsp;<span
                    style="background-color: #4a83ed; color: white; border-radius:5px; padding:5px; padding-left:7px; padding-right:7px"><i
                        class='bx bx-group'></i></span>&nbsp;&nbsp;přidáte {{currentFormData.givenName}} do první skupiny.
            </template>

        </vs-table>
            </div>
        </div>
        <br>
        <br>
        <br>
        <br>
        <vs-dialog width="550px" v-model="deleting">
            <template #header>
                <h4>
                    Připravujete se smazat uživatelský účet {{currentFormData.userPrincipalName}}!
                </h4>
            </template>


            <div class="con-content">
                <p>
                    Kliknutím na tlačítko Smazat smažete uživatelský účet uživatele {{currentFormData.cn}}! Jste si
                    naprosto jisti, že chcete účet smazat? TATO AKCE SE NEDÁ ODVOLAT!
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
                    Kliknutím na tlačítko Odstranit odeberete objekt ze skupiny {{currentFormData.cn}}. Pokud se rozhodnete objekt zpět přidat, klikněte na tlačítko přidat.
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
        components: {},
        data() {
            return {
                user: {

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
            async loadUser() {
                const loading = this.$vs.loading({
                    color: 'primary',
                    type: 'corners'
                })
                const hostname = this.$g('server_url') + "/v1/search?q=" + this.domainName.domainName.userDn +
                    "&domain=" + this.domainName.domainName.domainName +
                    "&auth=" + this.$store.state.token;

                const results = await axios.get(hostname);
                await new Promise((resolve) => setTimeout(resolve, 500));
                if (results.data.status === "success") {
                    //this.user = results.data.data[this.domainName.domainName.userDn];
                    this.currentFormData = results.data.data[this.domainName.domainName.userDn];
                    if (this.currentFormData.thumbnailPhoto != null || this.currentFormData.thumbnailPhoto !=
                        undefined) {
                        this.picture = true;
                    }
                    this.getObjects(this.currentFormData.memberOf);
                    
                }
                if (results.data === 401) {
                    this.openNotification('top-center', 'danger',
                        `<i class='bx bx-select-multiple' ></i>`, 'K tomuto zdroji nemáte přístup!',
                        'Upozornění zabezpečení FJDCS');
                }
                this.$Progress.finish();
                loading.close();
            },async addToGroup() {
                const loading = this.$vs.loading({
                    color: 'success',
                    type: 'corners'
                })


                var data = new FormData();
                data.append('dnToAdd', this.domainName.domainName.userDn);
                data.append('dn', this.addingObject);
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
                            this.loadUser();
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
            async deleteMe() {
                const loading = this.$vs.loading({
                    color: 'success',
                    type: 'corners'
                })


                var data = new FormData();
                data.append('dn', this.domainName.domainName.userDn);
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
                                `<i class='bx bx-select-multiple' ></i>`, 'Uživatel byl úspěšně smazán',
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
            async removeFromGroup() {
                const loading = this.$vs.loading({
                    color: 'success',
                    type: 'corners'
                })


                var data = new FormData();
                data.append('dnToRemove', this.domainName.domainName.userDn);
                data.append('dn', this.removingObject);
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
                                `<i class='bx bx-select-multiple' ></i>`, 'Objekt byl úspěšně odebrán ze skupiny',
                                'Operace úspěšná');
                                this.removing = false;
                                this.loadUser();

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
                    loading.close();

            },
            async changeFields() {
                const loading = this.$vs.loading({
                    color: 'success',
                    type: 'corners'
                })


                var data = new FormData();
                data.append('dn', this.domainName.domainName.userDn);
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
            handleChange(changedField) {
                const results = this.changedFields.filter((field) => field !== changedField);
                if (this.user[changedField] !== this.currentFormData[changedField]) {
                    results.push(changedField);
                }

                this.changedFields = results;
            },
        },
        created() {
            this.loadUser();
            this.$parent.activePage = "domainUser";
            console.log(this.$props)
        },
    }
</script>