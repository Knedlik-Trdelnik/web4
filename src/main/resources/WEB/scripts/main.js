import { createApp, ref, onMounted} from 'vue'



createApp({
    setup() {

        const username = ref('')
        const password = ref('')
        const message = ref('Введите логин и пароль')
        const isEnter = ref(false);
        const isLogin = ref(true);
        const regMessage = ref('');
        const rePassword = ref('');


        const login = async (event) => {

            event.preventDefault()

            message.value = 'Отправка данных...'

            const loginData = {
                username: username.value,
                password: password.value
            }


            try {
                const response = await axios.post('/auth/login', loginData)

                const token = response.data.token;
                localStorage.setItem("jwt_token", token)
                console.log('Успешный вход:', response.data)
                message.value = 'Успешный вход';
                isEnter.value = true;

            } catch (error) {

                console.error('Ошибка входа:', error)

                let errorMessage = ' Ошибка сети или сервера'

                if (error.response) {
                    errorMessage = error.response.data.message || `❌ Ошибка: ${error.response.status} ${error.response.statusText}`
                }

                message.value = errorMessage
            }
        }

        const registrate = async (event) => {

            event.preventDefault()

            regMessage.value = 'Отправка данных...'
            if (password.value != rePassword.value) {
                regMessage.value = 'Пароли должны совпадать!'
                return;
            }
            if (password.length == 0) {
                regMessage.value = 'Пароль должен быть!!!'
                return;
            }
            const loginData = {
                username: username.value,
                password: password.value
            }


            try {
                const response = await axios.post('/auth/register', loginData)


                console.log('Успешная регистрация', response.data)
                regMessage.value = 'Успешная регистрация!';

            } catch (error) {

                console.error('Ошибка входа:', error)

                let errorMessage = ' Ошибка сети или сервера'

                if (error.response) {

                    if (error.response.status == 418) {
                        errorMessage = error.response.data.message || `❌ Ошибка: имя занято`
                    }else {
                        errorMessage = error.response.data.message || `❌ Ошибка: ${error.response.status} ${error.response.statusText}`
                    }
                }

                regMessage.value = errorMessage
            }
        }
        const switchForm = () => {
            isLogin.value = !isLogin.value;

            username.value = '';
            password.value = '';
            rePassword.value = '';
            message.value = 'Введите логин и пароль';
            regMessage.value = '';
        }

        const disLogin = (event) => {
            localStorage.removeItem("jwt_token");
            isEnter.value = false;
            username.value = '';
            password.value = '';
            message.value = 'Вы вышли из системы';

        }
        onMounted(() => {
            const token = localStorage.getItem("jwt_token");
            if (token) {
                isEnter.value = true;
                message.value = "Вы уже авторизованы.";
            }
        })

        return {
            username,
            password,
            message,
            login,
            disLogin,
            isEnter,
            isLogin,
            regMessage,
            rePassword,
            registrate,
            switchForm
        }
    }
}).mount('#app')

