# 🚀 ENHANCED POST & COMMENT FEATURES

## 📋 Tổng quan

Hệ thống Post và Comment được nâng cấp với nhiều tính năng hiện đại và tương tác cao.

---

## 📝 ENHANCED POST FEATURES

### 1. **Advanced Post Creation**

#### Tạo post với đầy đủ tính năng
```http
POST /api/posts/create
Authorization: Bearer <token>
Content-Type: application/json

{
  "title": "Complete Guide to Node.js",
  "content": "Full article content here...",
  "excerpt": "Short description",
  "categoryId": 1,
  "tags": ["nodejs", "javascript", "backend"],
  "featuredImage": "https://example.com/image.jpg",
  "gallery": [
    "https://example.com/img1.jpg",
    "https://example.com/img2.jpg"
  ],
  "status": "published",
  "publishAt": "2024-04-15T10:00:00Z",
  "seoTitle": "Complete Guide to Node.js - 2024",
  "seoDescription": "Learn Node.js from scratch...",
  "allowComments": true,
  "isPinned": false,
  "isSponsored": false,
  "customFields": {
    "difficulty": "intermediate",
    "estimatedTime": "30 minutes"
  }
}
```

**Features:**
- ✅ Auto-generate slug from title
- ✅ SEO optimization fields
- ✅ Schedule publishing
- ✅ Custom fields support
- ✅ Auto-calculate reading time
- ✅ Gallery support
- ✅ Pin/Sponsor posts

---

### 2. **Post Interactions**

#### Like/Unlike Post
```http
POST /api/posts/:postId/like
Authorization: Bearer <token>
```

**Response:**
```json
{
  "success": true,
  "action": "liked",
  "message": "Post liked successfully"
}
```

#### Bookmark/Unbookmark Post
```http
POST /api/posts/:postId/bookmark
Authorization: Bearer <token>
```

**Features:**
- ✅ Organize bookmarks in collections
- ✅ Add personal notes
- ✅ Mark as read/unread
- ✅ Quick access to saved posts

#### Share Post
```http
POST /api/posts/:postId/share
Content-Type: application/json

{
  "platform": "twitter",
  "customMessage": "Check out this amazing article!"
}
```

**Supported platforms:**
- Facebook
- Twitter
- LinkedIn
- WhatsApp
- Telegram
- Email
- Copy Link

---

### 3. **Post Discovery**

#### Get Trending Posts
```http
GET /api/posts/trending/posts?period=7d&limit=10
```

**Trending Algorithm:**
```
Trending Score = (views × 0.3) + (likes × 0.5) + (shares × 0.2)
```

#### Advanced Search
```http
GET /api/posts/search/advanced?q=nodejs&category=1&tags=javascript&author=5&dateFrom=2024-01-01&dateTo=2024-04-01&sortBy=popular&page=1&limit=10
```

**Search Features:**
- ✅ Full-text search (title, content, excerpt)
- ✅ Filter by category
- ✅ Filter by tags (multiple)
- ✅ Filter by author
- ✅ Date range filter
- ✅ Multiple sort options (newest, oldest, popular, trending)
- ✅ Pagination

---

### 4. **Reading List & Bookmarks**

#### Get User Bookmarks
```http
GET /api/posts/user/bookmarks?page=1&limit=10
Authorization: Bearer <token>
```

#### Get Reading List
```http
GET /api/posts/user/reading-list?status=unread&page=1&limit=10
Authorization: Bearer <token>
```

---

### 5. **Post Details với Rich Data**

```http
GET /api/posts/:slug
```

**Response includes:**
```json
{
  "success": true,
  "post": {
    "id": 1,
    "title": "...",
    "content": "...",
    "author": {...},
    "category": {...},
    "comments": [...],
    "userInteractions": {
      "liked": true,
      "bookmarked": false
    },
    "relatedPosts": [...],
    "engagementStats": {
      "likes": 150,
      "shares": 45,
      "comments": 32,
      "views": 1250
    }
  }
}
```

---

## 💬 ENHANCED COMMENT FEATURES

### 1. **Advanced Comment Creation**

```http
POST /api/comments/create
Authorization: Bearer <token>
Content-Type: application/json

{
  "postId": 1,
  "content": "Great article! @john what do you think?",
  "parentId": null,
  "mentions": [5],
  "attachments": [
    {
      "type": "image",
      "url": "https://example.com/screenshot.png"
    }
  ],
  "isAnonymous": false
}
```

**Features:**
- ✅ Threaded comments (nested replies)
- ✅ Mention users with @username
- ✅ Attach images/files
- ✅ Anonymous commenting option
- ✅ Auto-moderation (pending approval)

---

### 2. **Comment Threading**

#### Get Threaded Comments
```http
GET /api/comments/post/:postId?page=1&limit=20&sortBy=newest
```

**Sort Options:**
- `newest` - Most recent first
- `oldest` - Oldest first
- `popular` - Most liked first

**Response Structure:**
```json
{
  "success": true,
  "comments": [
    {
      "id": 1,
      "content": "...",
      "user": {...},
      "likes": 15,
      "replies": [
        {
          "id": 2,
          "content": "...",
          "user": {...},
          "parentId": 1
        }
      ],
      "reactions": {
        "like": 10,
        "love": 3,
        "haha": 2
      }
    }
  ]
}
```

---

### 3. **Comment Interactions**

#### Like/Unlike Comment
```http
POST /api/comments/:commentId/like
Authorization: Bearer <token>
```

#### React to Comment (Emoji Reactions)
```http
POST /api/comments/:commentId/react
Authorization: Bearer <token>
Content-Type: application/json

{
  "reactionType": "love"
}
```

**Available Reactions:**
- 👍 `like`
- ❤️ `love`
- 😂 `haha`
- 😮 `wow`
- 😢 `sad`
- 😠 `angry`

---

### 4. **Comment Moderation**

#### Edit Comment
```http
PUT /api/comments/:commentId/edit
Authorization: Bearer <token>
Content-Type: application/json

{
  "content": "Updated comment content"
}
```

**Rules:**
- ✅ Can edit within 15 minutes
- ✅ Shows "edited" badge
- ✅ Admin can edit anytime

#### Delete Comment
```http
DELETE /api/comments/:commentId
Authorization: Bearer <token>
```

**Features:**
- ✅ Soft delete (status = 'deleted')
- ✅ Preserves thread structure
- ✅ Shows "[deleted]" placeholder

#### Report Comment
```http
POST /api/comments/:commentId/report
Authorization: Bearer <token>
Content-Type: application/json

{
  "reason": "spam",
  "description": "This comment is advertising unrelated products"
}
```

**Report Reasons:**
- `spam` - Spam or advertising
- `harassment` - Harassment or bullying
- `hate_speech` - Hate speech
- `misinformation` - False information
- `inappropriate` - Inappropriate content
- `other` - Other reasons

---

### 5. **Admin/Moderator Actions**

#### Pin Comment
```http
POST /api/comments/:commentId/pin
Authorization: Bearer <token>
```

**Who can pin:**
- Post author
- Admin
- Moderator

#### Moderate Comment
```http
POST /api/comments/:commentId/moderate
Authorization: Bearer <token>
Content-Type: application/json

{
  "action": "approve",
  "reason": "Content is appropriate"
}
```

**Actions:**
- `approve` - Approve pending comment
- `reject` - Reject comment
- `spam` - Mark as spam

---

### 6. **Comment Statistics**

```http
GET /api/comments/post/:postId/stats
```

**Response:**
```json
{
  "success": true,
  "stats": {
    "totalComments": 150,
    "approvedComments": 145,
    "pendingComments": 5,
    "topCommenters": [
      {
        "userId": 5,
        "user": {...},
        "commentCount": 25
      }
    ],
    "recentActivity": [...]
  }
}
```

---

## 🎯 Use Cases & Examples

### Use Case 1: Complete Blog Post Workflow

```javascript
// 1. Create post
const post = await createPost({
  title: "Node.js Best Practices",
  content: "...",
  tags: ["nodejs", "javascript"]
});

// 2. User reads and likes
await likePost(post.id);

// 3. User bookmarks for later
await bookmarkPost(post.id);

// 4. User comments
const comment = await createComment({
  postId: post.id,
  content: "Great article!"
});

// 5. Others react to comment
await reactToComment(comment.id, "love");

// 6. User shares on social media
await sharePost(post.id, "twitter");
```

### Use Case 2: Comment Thread Discussion

```javascript
// 1. User A comments
const commentA = await createComment({
  postId: 1,
  content: "What about async/await?"
});

// 2. User B replies
const commentB = await createComment({
  postId: 1,
  content: "@userA Great question! Here's how...",
  parentId: commentA.id,
  mentions: [userA.id]
});

// 3. User C reacts
await reactToComment(commentB.id, "wow");

// 4. Author pins helpful comment
await pinComment(commentB.id);
```

### Use Case 3: Content Discovery

```javascript
// 1. Find trending posts
const trending = await getTrendingPosts({ period: '7d' });

// 2. Advanced search
const results = await advancedSearch({
  q: "react hooks",
  tags: ["react", "javascript"],
  sortBy: "popular"
});

// 3. Get personalized reading list
const readingList = await getReadingList();
```

---

## 📊 Database Schema

### New Tables Created:

1. **post_views** - Track unique views
2. **post_likes** - Track post likes
3. **post_bookmarks** - User bookmarks with collections
4. **post_shares** - Track social shares
5. **comment_reactions** - Emoji reactions on comments
6. **comment_reports** - User reports on comments
7. **comment_mentions** - Track @mentions in comments

---

## 🔒 Security Features

### Rate Limiting
- Post creation: 10 posts/minute
- Comment creation: 20 comments/minute
- Comment reports: 5 reports/hour

### Access Control
- Role-based permissions (admin, moderator, author, user)
- Resource ownership validation
- Edit time limits (15 minutes for comments)

### Content Moderation
- Auto-pending for new comments
- Report system with admin review
- Spam detection
- Profanity filtering (can be added)

---

## 🚀 Performance Optimizations

### Caching Strategy
- Cache trending posts (5 minutes)
- Cache post details (2 minutes)
- Cache comment threads (1 minute)

### Database Indexes
- Composite indexes on (postId, userId)
- Index on createdAt for time-based queries
- Full-text index on content fields

### Pagination
- All list endpoints support pagination
- Default limit: 10-20 items
- Max limit: 100 items

---

## 📈 Analytics & Metrics

### Post Metrics
- Views (unique per user/IP per 24h)
- Likes
- Shares
- Comments
- Bookmarks
- Reading time
- Engagement rate

### Comment Metrics
- Total comments
- Approved/Pending/Rejected
- Reactions breakdown
- Top commenters
- Response rate
- Average response time

---

## 🎨 Frontend Integration Examples

### React Component Example

```jsx
// PostCard Component
function PostCard({ post }) {
  const [liked, setLiked] = useState(post.userInteractions.liked);
  const [bookmarked, setBookmarked] = useState(post.userInteractions.bookmarked);

  const handleLike = async () => {
    await fetch(`/api/posts/${post.id}/like`, {
      method: 'POST',
      headers: { 'Authorization': `Bearer ${token}` }
    });
    setLiked(!liked);
  };

  const handleBookmark = async () => {
    await fetch(`/api/posts/${post.id}/bookmark`, {
      method: 'POST',
      headers: { 'Authorization': `Bearer ${token}` }
    });
    setBookmarked(!bookmarked);
  };

  return (
    <div className="post-card">
      <h2>{post.title}</h2>
      <p>{post.excerpt}</p>
      <div className="actions">
        <button onClick={handleLike}>
          {liked ? '❤️' : '🤍'} {post.likes}
        </button>
        <button onClick={handleBookmark}>
          {bookmarked ? '🔖' : '📑'}
        </button>
        <ShareButton postId={post.id} />
      </div>
    </div>
  );
}

// CommentThread Component
function CommentThread({ postId }) {
  const [comments, setComments] = useState([]);
  const [sortBy, setSortBy] = useState('newest');

  useEffect(() => {
    fetchComments();
  }, [sortBy]);

  const fetchComments = async () => {
    const response = await fetch(
      `/api/comments/post/${postId}?sortBy=${sortBy}`
    );
    const data = await response.json();
    setComments(data.comments);
  };

  return (
    <div className="comment-thread">
      <CommentSort value={sortBy} onChange={setSortBy} />
      {comments.map(comment => (
        <Comment key={comment.id} comment={comment} />
      ))}
    </div>
  );
}
```

---

## 🔧 Migration Scripts

```bash
# Create new tables
npx sequelize-cli migration:generate --name create-post-interactions
npx sequelize-cli migration:generate --name create-comment-enhancements

# Run migrations
npm run db:migrate
```

---

**🎉 Chúc mừng! Blog của bạn giờ có hệ thống Post & Comment enterprise-level!**