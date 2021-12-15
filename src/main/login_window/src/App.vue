<template>
  <div class="fdlf-loginbox">
    <div class="columns is-gapless is-mobile fdlf-loginbox-cols">
      <div class="column is-half is-hidden-mobile fdlf-loginbox-image">
        <div
          class="fdlf-loginbox-credits uk-position-bottom-left uk-position-small"
        ></div>
      </div>
      <div class="column is-half is-full-mobile uk-position-relative">
        <transition name="slide" mode="out-in">
          <div class="username-action" v-if="insertusername">
            <div class="fdlf-loginbox-waterfall">
              <div class="fdlflbwf-logo">
                <img
                  :src="system.logo"
                  :key="system.logo"
                  id="system-logo"
                  rel="preload"
                />
              </div>
              <div class="spacer"></div>
              <div class="fdlflbwf-action">
                <label><b>Uživatelské jméno</b></label>
                <div
                  class="field inputs has-addons"
                  v-bind:class="
                    system.domainwrong || system.usernamewrong
                      ? 'shake-horizontal'
                      : ''
                  "
                >
                  <div class="control">
                    <input
                      class="input"
                      type="text"
                      v-model="login.username"
                      placeholder="Vaše uživatelské jméno s doménou"
                      v-on:keyup.enter="checkDomain"
                    />
                  </div>
                  <div class="control">
                    <button class="button is-dark" v-on:click="checkDomain">
                      Další
                    </button>
                  </div>
                </div>
                <p class="has-text-danger" v-if="system.domainwrong">
                  Tato doména v systému neexistuje
                </p>
                <p class="has-text-danger" v-if="system.usernamewrong">
                  Toto uživatelské jméno se nenachází v doméně
                  {{ domain.domainname }}
                </p>
              </div>
              <div class="spacer"></div>
              <div class="fdlflbwf-comments">
                <b>Jaké uživatelské jméno mám použít?</b>
                <p>
                  Můžete použít uživatelské jméno ve formátu
                  <i>DOMÉNA\uživatelské_jméno</i>, či
                  <i>uživatelské_jméno@doména.tld</i>.
                </p>
                <br />
                <p>
                  Pokud neznáte svou doménu, kontaktujte vašeho síťového
                  správce.
                </p>
              </div>
            </div>
          </div>
        </transition>
        <transition name="slide" mode="out-in">
          <div class="password-action" v-if="insertpassword">
            <div class="fdlf-loginbox-waterfall">
              <div class="fdlflbwf-logo">
                <img
                  :src="domain.logo"
                  :key="domain.logo"
                  id="domain-logo"
                  @load="domainlogoload"
                />
              </div>
              <div class="spacer"></div>
              <div class="fdlflbwf-action">
                <label
                  ><b>{{ domain.name }}</b>
                  &nbsp;
                  <small>{{ domain.user }}</small>
                </label>
                <div
                  class="field inputs has-addons"
                  v-bind:class="system.passwordwrong ? 'shake-horizontal' : ''"
                >
                  <div class="control">
                    <input
                      class="input"
                      type="password"
                      v-model="login.password"
                      placeholder="Heslo"
                      v-on:keyup.enter="logon"
                    />
                  </div>
                  <div class="control">
                    <button class="button is-primary" v-on:click="logon">
                      Přihlásit
                    </button>
                  </div>
                </div>
                <p class="has-text-danger" v-if="system.passwordempty">
                  Vyplňte heslo
                </p>
                <p class="has-text-danger" v-if="system.passwordwrong">
                  Špatné přihlašovací údaje
                </p>
              </div>
              <div class="spacer"></div>
              <div class="fdlflbwf-comments">
                <b>Zapomněli jste heslo?</b>
                <p>
                  Pokud jste zapomněli heslo, můžete kontaktovat síťového
                  administrátora, správce domény {{ domain.domainname }}, či
                  správce organizace {{ domain.organizationname }}.
                </p>
              </div>
            </div>
          </div>
        </transition>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
export default {
  name: "App",
  components: {},
  data() {
    return {
      insertusername: true,
      insertpassword: false,
      system: {
        logo: "http://cdn.vespotok.net/img/fujindomains.svg",
        debug: "",
        loginname: "",
        logindomain: "",
        domainwrong: false,
        usernamewrong: false,
        passwordempty: false,
        passwordwrong: false,
      },
      domain: {
        logo: "",
        name: "",
        user: "",
        organizationname: "",
        domainname: "",
      },
      login: {
        username: "",
        password: "",
      },
    };
  },
  methods: {
    async checkDomain() {
      this.system.domainwrong = false;
      this.system.usernamewrong = false;
      const hostname =
        "http://auth.dc.vespotok.net:8080/api/v1/domain?username=" +
        encodeURIComponent(this.login.username);
      this.system.debug = hostname;
      const results = await axios.get(hostname);
      await new Promise((resolve) => setTimeout(resolve, 500));

      if (results.data.status === "success") {
        this.domain.logo = results.data.domain.organizationLogo;
        this.domain.domainname = results.data.domain.domainName;
        this.domain.organizationname = results.data.domain.organizationName;
        this.domain.name = results.data.user.name;
        this.domain.user = results.data.user.samLogin;
        if (Object.keys(results.data.user).length !== 0) {
          this.insertusername = false;
          await new Promise((resolve) => setTimeout(resolve, 500));
          this.insertpassword = true;
        } else {
          this.system.usernamewrong = true;
        }
      } else {
        this.system.domainwrong = true;
      }
    },
    async logon() {
      this.system.passwordwrong = false;

      if (this.login.password === "") {
        this.system.passwordempty = true;
      } else {
        this.system.passwordempty = false;
        const hostname =
          "http://auth.dc.vespotok.net:8080/api/v1/login?domain=" +
          this.domain.domainname +
          "&username=" +
          encodeURIComponent(this.login.username) +
          "&password=" +
          this.login.password;
        const results = await axios.get(hostname);
        await new Promise((resolve) => setTimeout(resolve, 500));
        if (results.data.status === "success") {
          alert(results.data.token);
        } else {
          this.system.passwordwrong = true;
        }
      }
    },
    async domainlogoload() {},
  }
};
</script>
