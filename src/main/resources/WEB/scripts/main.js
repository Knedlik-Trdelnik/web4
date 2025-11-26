import { createApp, ref } from 'vue'

// Предполагается, что библиотека axios уже подключена в HTML через CDN

createApp({
    setup() {
        // 1. Реактивные переменные для данных формы
        const username = ref('')
        const password = ref('')
        const message = ref('Введите логин и пароль')

        // 2. Функция для обработки отправки формы
        const login = async (event) => {
            // Предотвращаем стандартное поведение отправки формы (перезагрузку страницы)
            event.preventDefault()

            message.value = 'Отправка данных...'

            // Данные для отправки
            const loginData = {
                username: username.value,
                password: password.value
            }

            // Отправка POST-запроса
            try {
                // !!! ВАЖНО: Замените '/your-api-endpoint/login' на фактический URL вашего сервера !!!
                const response = await axios.post('/auth/login', loginData)

                // Обработка успешного ответа
                console.log('Успешный вход:', response.data)
                message.value = '✅ Успешный вход! Токен: ' + (response.data.token || 'Нет токена')

                // Опционально: сохраните токен (например, в localStorage)
                // localStorage.setItem('authToken', response.data.token)

            } catch (error) {
                // Обработка ошибки
                console.error('Ошибка входа:', error)

                let errorMessage = '❌ Ошибка сети или сервера'

                // Если есть ответ от сервера (например, 401 Unauthorized)
                if (error.response) {
                    // Используем сообщение об ошибке от сервера, если оно есть
                    errorMessage = error.response.data.message || `❌ Ошибка: ${error.response.status} ${error.response.statusText}`
                }

                message.value = errorMessage
            }
        }

        return {
            username,
            password,
            message,
            login // Передаем функцию login в шаблон
        }
    }
}).mount('#app')