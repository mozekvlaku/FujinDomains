<template>
    <div>

        <div style="display:flex; align-items:center">
            <vs-avatar @click="$router.go(-1)">
                <i class='bx bx-arrow-back'></i>
            </vs-avatar>
            <vs-avatar primary @click="handleSubmit" style="margin-left:15px; cursor:pointer">
                <i class='bx bx-save'></i>
            </vs-avatar>
            <vs-avatar dark style="margin-left:15px">
                <i class='bx bx-plus'></i>
            </vs-avatar>
            <h1 style="margin-left:15px">Přidání nového uživatele
            </h1>

        </div>
        <br>

        <div class="columns">
            <div class="column is-narrow">
                <vs-avatar square size="320" primary style="font-size: 5rem;">
                    <img :src="newUser.thumbnailPhoto"  v-if="newUser.thumbnailPhoto !== ''">
                    <template #text v-if="newUser.thumbnailPhoto === ''">
                        {{newUser.cn}}
                    </template>
                    
                    <i class='bx bx-user bx-lg' v-if="newUser.thumbnailPhoto === '' && newUser.cn === ''"></i>
                </vs-avatar><br>
                <br>
                <vs-input type="text" border style="width:100%" v-model="newUser.thumbnailPhoto"
                    label="URL profilové fotky" />
            </div>
            <div class="column ">
                <h3 class="is-marginless is-paddingless">Obecné</h3>
                <br>
                <div class="columns is-multiline">
                    <div class="column is-narrow">
                        <vs-input type="text" @input="fillingName" v-model="newUser.givenName" label="Jméno" />
                    </div>
                    <div class="column is-narrow">
                        <vs-input type="text" @input="fillingName" v-model="newUser.sn" label="Příjmení" />
                    </div>
                    <div class="column is-narrow">
                        <vs-input type="text" v-model="newUser.initials" label="Iniciály dalších jmen" />
                    </div>
                    <div class="column is-narrow">
                        <vs-input type="text" v-model="newUser.cn" label="Zobrazované jméno" />
                    </div>
                    <div class="column is-narrow">
                        <vs-input type="text" v-model="newUser.comment" label="Popis" />
                    </div>
                    <div class="column is-narrow">
                        <vs-input type="text" v-model="newUser.physicalDeliveryOfficeName" label="Kancelář" />
                    </div>
                    <div class="column is-narrow">
                        <vs-input type="text" v-model="newUser.telephoneNumber" label="Telefonní číslo"><template #icon>
                                <i class='bx bx-phone'></i>
                            </template></vs-input>
                    </div>
                    <div class="column is-narrow">
                        <vs-input type="text" v-model="newUser.mail" label="Email"><template #icon>
                                <i class='bx bx-mail-send'></i>
                            </template></vs-input>
                    </div>
                    <div class="column is-narrow">
                        <vs-input type="text" v-model="newUser.wwwHomePage" label="Webová stránka"><template #icon>
                                <i class='bx bxl-internet-explorer'></i>
                            </template></vs-input>
                    </div>
                </div>
                <h3 class="is-marginless is-paddingless">Adresa</h3>
                <br>
                <div class="columns is-multiline">
                    <div class="column is-narrow">
                        <vs-input type="text" v-model="newUser.streetAddress" label="Ulice" />
                    </div>
                    <div class="column is-narrow">
                        <vs-input type="text" v-model="newUser.postOfficeBox" label="Poštovní přihrádka" />
                    </div>
                    <div class="column is-narrow">
                        <vs-input type="text" v-model="newUser.l" label="Město" />
                    </div>
                    <div class="column is-narrow">
                        <vs-input type="text" v-model="newUser.st" label="Okres" />
                    </div>
                    <div class="column is-narrow">
                        <vs-input type="text" v-model="newUser.postalCode" label="PSČ" />
                    </div>
                    <div class="column is-narrow">
                        <vs-input type="text" v-model="newUser.c" label="Země/oblast" />
                    </div>
                </div>
                <h3 class="is-marginless is-paddingless">Účet</h3>
                <br>
                <div class="columns is-multiline">
                    <div class="column is-narrow">
                        <vs-input type="text" v-model="newUser.userPrincipalName" @input="fillingUsername"
                            label="Přihlašovací jméno"><template #icon>
                                <i class='bx bx-user'></i>
                            </template></vs-input>
                    </div>
                    <div class="column is-narrow">
                        <vs-input type="text" v-model="newUser.samAccountName" label="Přihlašovací jméno (NT4)">
                            <template #icon>
                                <i class='bx bx-user'></i>
                            </template></vs-input>
                    </div>
                    <div class="column is-narrow">
                        <vs-input type="password" v-model="newUser.userPassword" label-placeholder="Heslo"
                            :visiblePassword="hasVisiblePassword" icon-after
                            @click-icon="hasVisiblePassword = !hasVisiblePassword">
                            <template #icon>
                                <i v-if="!hasVisiblePassword" class='bx bx-show-alt'></i>
                                <i v-else class='bx bx-hide'></i>
                            </template>


                        </vs-input>
                    </div>
                </div>
                <h3 class="is-marginless is-paddingless">Profil</h3>
                <br>
                <div class="columns is-multiline">
                    <div class="column is-narrow">
                        <vs-input type="text" v-model="newUser.fujinSh" label="Textový shell Fujinu"><template #icon>
                                <i class='bx bx-terminal'></i>
                            </template></vs-input>
                    </div>
                    <div class="column is-narrow">
                        <vs-input type="text" v-model="newUser.fujinGui" label="Grafický shell Fujin"><template #icon>
                                <i class='bx bx-mouse-alt'></i>
                            </template></vs-input>
                    </div>
                    <div class="column is-narrow">
                        <vs-input type="text" v-model="newUser.fujinHome" label="Domovská složka Fujinu"><template
                                #icon>
                                <i class='bx bx-home'></i>
                            </template></vs-input>
                    </div>
                </div>
                <h3 class="is-marginless is-paddingless">Telefony</h3>
                <br>
                <div class="columns is-multiline">
                    <div class="column is-narrow">
                        <vs-input type="text" v-model="newUser.homePhone" label="Domů" />
                    </div>
                    <div class="column is-narrow">
                        <vs-input type="text" v-model="newUser.otherTelephone" label="Operátor" />
                    </div>
                    <div class="column is-narrow">
                        <vs-input type="text" v-model="newUser.mobile" label="Mobilní telefon" />
                    </div>
                    <div class="column is-narrow">
                        <vs-input type="text" v-model="newUser.fax" label="Fax" />
                    </div>
                    <div class="column is-narrow">
                        <vs-input type="text" v-model="newUser.ipPhone" label="Telefon IP" />
                    </div>
                </div>
            </div>
        </div>

    </div>
</template>
<script>
    import axios from "axios";
    export default {
        name: 'Domains',
        components: {},
        data() {
            return {
                hasVisiblePassword: false,
                newUser: {
                    fujinSh: "/bin/sh",
                    fujinGui: "/usr/local/raijin/raijin.appf",
                    fujinHome: "",
                    ipPhone: "",
                    fax: "",
                    mobile: "",
                    otherTelephone: "",
                    homePhone: "",
                    thumbnailPhoto: "",
                    givenName: "",
                    sn: "",
                    initials: "",
                    cn: "",
                    comment: "",
                    physicalDeliveryOfficeName: "",
                    telephoneNumber: "",
                    mail: "",
                    wwwHomePage: "",
                    streetAddress: "",
                    postOfficeBox: "",
                    l: "",
                    st: "",
                    postalCode: "",
                    c: "",
                    userPrincipalName: "",
                    samAccountName: ""
                },
                picture: false
            };
        },
        props: {
            domainName: Object
        },
        methods: {
            async handleSubmit() {
                const loading = this.$vs.loading({
                    color: 'success',
                    type: 'corners'
                })


                var data = new FormData();
                data.append('objectclass', 'person');
                data.append('domain', this.domainName.domainName.domainName);
                data.append('auth', this.$store.state.token);
                data.append('data', JSON.stringify(this.newUser));
                const hostname = this.$g('server_url') + "/v1/add";
                axios.post(hostname, data, {
                        headers: {
                            'Content-Type': 'application/json'
                        }
                    })
                    .then((response) => {
                        if (response.data.status === "success") {
                            this.openNotification('top-center', 'success',
                                `<i class='bx bx-select-multiple' ></i>`,
                                'Uživatel byl úspěšně přidán', 'Operace úspěšná');
                                this.$router.go(-1)
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
            fillingName() {
                this.newUser.cn = this.newUser.givenName + " " + this.newUser.sn;
                let rgx = new RegExp(/(\p{L}{1})\p{L}+/, 'gu');
                let initials = [...this.newUser.cn.matchAll(rgx)] || [];
                this.newUser.initials = ((initials.shift()?.[1] || '') + (initials.pop()?.[1] || '')).toUpperCase();
            },
            fillingUsername() {
                this.newUser.fujinHome = "/home/" + this.newUser.userPrincipalName + "/";
                this.newUser.samAccountName = this.newUser.userPrincipalName;
            }
        },
        created() {
            this.$parent.activePage = "domainLink";
        },
    }
    
</script>