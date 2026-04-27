// Chatbot Widget - Modern UI
class ChatbotWidget {
    constructor() {
        this.sessionId = null;
        this.isOpen = false;
        this.messages = [];
        this.init();
    }

    init() {
        this.createWidget();
        this.attachEventListeners();
    }

    createWidget() {
        const widgetHTML = `
            <div id="chatbot-widget" class="chatbot-widget">
                <!-- Chat Button -->
                <button id="chat-toggle-btn" class="chat-toggle-btn">
                    <svg class="chat-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                        <path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"></path>
                    </svg>
                    <span class="chat-badge">1</span>
                </button>

                <!-- Chat Window -->
                <div id="chat-window" class="chat-window hidden">
                    <!-- Header -->
                    <div class="chat-header">
                        <div class="chat-header-info">
                            <div class="bot-avatar">🤖</div>
                            <div>
                                <h3 class="bot-name">Blog Assistant</h3>
                                <p class="bot-status">
                                    <span class="status-dot"></span>
                                    Online
                                </p>
                            </div>
                        </div>
                        <button id="chat-close-btn" class="chat-close-btn">
                            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor">
                                <path d="M18 6L6 18M6 6l12 12"></path>
                            </svg>
                        </button>
                    </div>

                    <!-- Messages -->
                    <div id="chat-messages" class="chat-messages">
                        <div class="chat-welcome">
                            <div class="welcome-icon">👋</div>
                            <h4>Chào mừng bạn!</h4>
                            <p>Tôi có thể giúp bạn tìm kiếm bài viết, trả lời câu hỏi và nhiều hơn nữa.</p>
                        </div>
                    </div>

                    <!-- Suggestions -->
                    <div id="chat-suggestions" class="chat-suggestions hidden"></div>

                    <!-- Input -->
                    <div class="chat-input-container">
                        <div class="chat-input-wrapper">
                            <input 
                                type="text" 
                                id="chat-input" 
                                class="chat-input" 
                                placeholder="Nhập tin nhắn..."
                                autocomplete="off"
                            />
                            <button id="chat-send-btn" class="chat-send-btn">
                                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor">
                                    <path d="M22 2L11 13M22 2l-7 20-4-9-9-4 20-7z"></path>
                                </svg>
                            </button>
                        </div>
                        <div class="chat-powered">
                            Powered by AI ✨
                        </div>
                    </div>
                </div>
            </div>
        `;

        document.body.insertAdjacentHTML('beforeend', widgetHTML);
    }

    attachEventListeners() {
        const toggleBtn = document.getElementById('chat-toggle-btn');
        const closeBtn = document.getElementById('chat-close-btn');
        const sendBtn = document.getElementById('chat-send-btn');
        const input = document.getElementById('chat-input');

        toggleBtn.addEventListener('click', () => this.toggleChat());
        closeBtn.addEventListener('click', () => this.closeChat());
        sendBtn.addEventListener('click', () => this.sendMessage());
        input.addEventListener('keypress', (e) => {
            if (e.key === 'Enter') this.sendMessage();
        });
    }

    async toggleChat() {
        const chatWindow = document.getElementById('chat-window');
        const badge = document.querySelector('.chat-badge');
        
        if (this.isOpen) {
            this.closeChat();
        } else {
            chatWindow.classList.remove('hidden');
            this.isOpen = true;
            badge.style.display = 'none';

            // Initialize session if not exists
            if (!this.sessionId) {
                await this.initSession();
            }
        }
    }

    closeChat() {
        const chatWindow = document.getElementById('chat-window');
        chatWindow.classList.add('hidden');
        this.isOpen = false;
    }

    async initSession() {
        try {
            const response = await fetch('/api/chatbot/init', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({})
            });

            const data = await response.json();
            if (data.success) {
                this.sessionId = data.session.sessionId;
                this.addMessage('bot', data.session.welcomeMessage);
            }
        } catch (error) {
            console.error('Failed to init chat:', error);
        }
    }

    async sendMessage() {
        const input = document.getElementById('chat-input');
        const message = input.value.trim();

        if (!message) return;

        // Add user message to UI
        this.addMessage('user', message);
        input.value = '';

        // Show typing indicator
        this.showTypingIndicator();

        try {
            const response = await fetch('/api/chatbot/message', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    sessionId: this.sessionId,
                    message
                })
            });

            const data = await response.json();
            
            // Remove typing indicator
            this.removeTypingIndicator();

            if (data.success) {
                this.addMessage('bot', data.botResponse.message, data.botResponse);
                
                // Show suggestions if any
                if (data.botResponse.suggestions) {
                    this.showSuggestions(data.botResponse.suggestions);
                }
            }
        } catch (error) {
            this.removeTypingIndicator();
            this.addMessage('bot', 'Xin lỗi, có lỗi xảy ra. Vui lòng thử lại.');
        }
    }

    addMessage(sender, text, data = {}) {
        const messagesContainer = document.getElementById('chat-messages');
        const messageDiv = document.createElement('div');
        messageDiv.className = `chat-message ${sender}-message`;

        const time = new Date().toLocaleTimeString('vi-VN', { 
            hour: '2-digit', 
            minute: '2-digit' 
        });

        messageDiv.innerHTML = `
            <div class="message-content">
                ${sender === 'bot' ? '<div class="message-avatar">🤖</div>' : ''}
                <div class="message-bubble">
                    <div class="message-text">${this.formatMessage(text)}</div>
                    <div class="message-time">${time}</div>
                    ${sender === 'bot' && data.id ? this.createFeedbackButtons(data.id) : ''}
                </div>
            </div>
        `;

        messagesContainer.appendChild(messageDiv);
        messagesContainer.scrollTop = messagesContainer.scrollHeight;

        this.messages.push({ sender, text, time });
    }

    formatMessage(text) {
        // Convert markdown-like syntax to HTML
        return text
            .replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>')
            .replace(/\n/g, '<br>')
            .replace(/🔗 (\/posts\/[\w-]+)/g, '<a href="$1" target="_blank">Xem bài viết →</a>');
    }

    createFeedbackButtons(messageId) {
        return `
            <div class="message-feedback">
                <button class="feedback-btn" onclick="chatbot.ratMessage(${messageId}, 'helpful')">
                    👍 Hữu ích
                </button>
                <button class="feedback-btn" onclick="chatbot.rateMessage(${messageId}, 'not_helpful')">
                    👎 Không hữu ích
                </button>
            </div>
        `;
    }

    showSuggestions(suggestions) {
        const suggestionsContainer = document.getElementById('chat-suggestions');
        suggestionsContainer.innerHTML = suggestions.map(suggestion => `
            <button class="suggestion-btn" onclick="chatbot.useSuggestion('${suggestion}')">
                ${suggestion}
            </button>
        `).join('');
        suggestionsContainer.classList.remove('hidden');
    }

    useSuggestion(suggestion) {
        const input = document.getElementById('chat-input');
        input.value = suggestion;
        this.sendMessage();
        document.getElementById('chat-suggestions').classList.add('hidden');
    }

    showTypingIndicator() {
        const messagesContainer = document.getElementById('chat-messages');
        const typingDiv = document.createElement('div');
        typingDiv.className = 'typing-indicator';
        typingDiv.id = 'typing-indicator';
        typingDiv.innerHTML = `
            <div class="message-avatar">🤖</div>
            <div class="typing-dots">
                <span></span>
                <span></span>
                <span></span>
            </div>
        `;
        messagesContainer.appendChild(typingDiv);
        messagesContainer.scrollTop = messagesContainer.scrollHeight;
    }

    removeTypingIndicator() {
        const typingIndicator = document.getElementById('typing-indicator');
        if (typingIndicator) {
            typingIndicator.remove();
        }
    }

    async rateMessage(messageId, rating) {
        try {
            await fetch(`/api/chatbot/messages/${messageId}/rate`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ rating })
            });
        } catch (error) {
            console.error('Failed to rate message:', error);
        }
    }
}

// Initialize chatbot when DOM is ready
let chatbot;
document.addEventListener('DOMContentLoaded', () => {
    chatbot = new ChatbotWidget();
});