import {createApp} from 'vue'

createApp({
    data() {
        return {
            dots: [],
            status: "Бездействие...",
            awesome: true,
            pageNumber: 1
        }
    },
    methods: {
        async loadDots() {
            try {
                const pageData = {
                    pageNumber: this.pageNumber,
                }
                const response = await axios.post("dots/data",pageData);
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


