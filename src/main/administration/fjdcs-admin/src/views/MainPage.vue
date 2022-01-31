<template>
  <div class="grid">
    <br><br>
    <br><br>
    <vs-row align="center" justify="center">
      <vs-col w="6" style="text-align:center">
        <div class="center con-avatars">
          <vs-avatar size="90" style="margin:auto;font-size: 2rem;"  :loading="isLoaded">
            
            <img :src="user.thumbnailPhoto"  v-if="profile">
            <template #text v-if="!profile">
                {{user.name}}
            </template>
          </vs-avatar>
          
          <h1>
            Vítejte, {{user.name}}
          </h1>
          <p>Zde spravujte své údaje pro přihlašování k webům, systému Fujin a operačním systémům Windows (vyžaduje Fujin Credential Provider Service for Windows) a macOS (vyžaduje nastavení síťových účtů). </p>
        </div>

      </vs-col>
    </vs-row> 
    <br>
    <vs-row align="center" justify="center">
      <vs-col w="4" vs-type="flex" vs-justify="center" vs-align="center" style="justify-content: center;display: flex;">
        <vs-card type="3">
    <template #title>
      <h3>Zabezpečte si účet</h3>
    </template>
    <template #img>
      <img src="https://cdn.vespotok.net/img/domains-security.png" alt="">
    </template>
    <template #text>
      <p>
        Internet je plný rizik, chraňte se před napadením aktivní změnou hesla a používáním dvoufaktorového ověřování.
      </p>
    </template>
    <template #interactions>
      <vs-button danger icon>
        <i class='bx bx-lock-open-alt'></i>
        <span class="span">
          Zabezpečení
        </span>
      </vs-button>
      
    </template>
  </vs-card>
      </vs-col>
      <vs-col w="4" vs-type="flex" vs-justify="center" vs-align="center" style="justify-content: center;display: flex;">
        <vs-card type="3" @click="$router.push({ path: '/federations' })">
    <template #title>
      <h3>Zkontrolujte federace</h3>
    </template>
    <template #img>
      <img src="https://cdn.vespotok.net/img/domains-federations.png" alt="">
    </template>
    <template #text>
      <p>
        Federace účtu znamená propojení, propojte svůj účet s Facebookem, LinkedInem, Google účtem, či spravujte své propojení s doménovými službami Active Directory.
      </p>
    </template>
    <template #interactions>
      <vs-button danger icon>
        <i class='bx bx-shape-triangle'></i>
        <span class="span">
          Federace
        </span>
      </vs-button>
      
    </template>
  </vs-card>
      </vs-col>
      
    </vs-row>
  </div>
</template>
<script>
  export default {
    name: 'MainPage',
    components: {

    },
    data() {
      return {
        user: this.$store.state,
        isLoaded: true,
        profile: true
      }
    },
    created() {
      this.$parent.searchingDomain = false;
      this.$parent.activePage = "/";
      this.isLoaded = false;
      if(this.user.thumbnailPhoto == undefined || this.user.thumbnailPhoto == "")
      {
        this.profile = false;
      }
    },
  }
</script>