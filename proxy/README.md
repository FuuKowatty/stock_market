# 🛠️ Nginx Proxy – Local Setup

Quick guide to setting up the local reverse proxy.

## 1. ⚙️ Configuration
Navigate to the `conf/` directory:
1. Create a copy of `nginx.conf.template`.
2. Rename it to `nginx.conf`.
3. Adjust properties proxy_pass should point to actually backend app.

## 2. 🔒 SSL Certificates
You can generate a self-signed certificate for `localhost` and place the files in the root `proxy/` directory.

**Required (Git Bash / CMD with OpenSSL):**
```bash
cd ./proxy # make sure you are in valid directory
openssl req -x509 -nodes -days 365 -newkey rsa:2048 -keyout localhost.key -out localhost.crt -subj "/CN=localhost"