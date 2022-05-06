class ApiService {
    /**
     * @type string
     */
    sessionToken;

    async login(username, password) {
        return this.#request('/api/users/login', 'POST', {
            username: username,
            password: password
        })
    }

    async getProfile() {
        return this.#request('/api/users/me')
    }

    async getRecipes() {
        return this.#request('/api/recipes')
    }

    async #request(url, method, body) {
        let headers = {}
        if (this.sessionToken) {
            headers['Authorization'] = `Bearer ${this.sessionToken}`
        }
        if (body && typeof body !== "string") {
            body = JSON.stringify(body)
            headers['Content-Type'] = 'application/json'
        }
        return fetch(url, {
            method: method,
            headers: headers,
            body: body
        }).then(async response => {
            if (response.status === 401) {
                console.error('401', await response.json())
                throw new Error('Invalid credentials');
            }
            if (!response.ok) {
                const errorResponse = await response.json()
                console.error('Request failed', errorResponse)
                throw new Error(errorResponse.message);
            }
            return await response.json()
        })
    }
}

export const apiService = new ApiService();