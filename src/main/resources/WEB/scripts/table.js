import {createApp} from 'vue'

createApp({
    data() {
        return {
            dots: [],
            status: "Бездействие...",
            awesome: true
        }
    },
    methods: {
        async loadDots() {
            try {
                const response = await axios.get("dots/");
                this.dots = response.data;
                console.log("Успешная загрузка: " + response.data);
                this.status = "Точки успешно загружены!"
            } catch (error) {
                if (error.response.status == 401) {
                    console.error(error)
                    this.status ='у вас нет доступа к результатам';
                }else {
                    console.error(error)
                    this.status = error.message;
                }
            }
        }
    }
}).mount('#app');


