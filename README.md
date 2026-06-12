<p align="center">
  <img src="src/main/resources/images/logo.png" alt="ClothingStore logo" width="160" />
</p>

<h1 align="center">ClothingStore — Boutique Manager</h1>

<p align="center">
  A clean, professional desktop point-of-sale and inventory manager for small clothing retailers.<br/>
  Built with JavaFX, Hibernate and Supabase (hosted PostgreSQL).
</p>

---

## Features

- **Owner login** — secure username + password sign-in with SHA-256 password hashing; the owner (admin) and employees see role-appropriate dashboards.
- **Team management** — the owner creates employee accounts straight from the Admin Dashboard.
- **Product catalogue** — add, update and delete products with category, size, stock quantity, unit price and supplier.
- **Supplier directory** — manage the partners that keep your racks stocked.
- **Point of sale** — register customers, build a cart, automatic stock checks, and place orders that atomically deduct stock.
- **Order history** — every sale at a glance, with order count and total revenue.
- **Cloud database** — all data lives in your own free Supabase (PostgreSQL) project, so it is backed up and accessible from any machine.
- **Modern business UI** — a cohesive light theme with a deep indigo accent, dark navigation sidebar, card-based layouts, subtle shadows, hover states and clean data tables.

## Tech stack

| Layer | Technology |
|---|---|
| UI | JavaFX 21, FXML, shared CSS theme |
| Business logic | BO layer (`BoFactory` + custom BOs) |
| Data access | DAO layer (`DaoFactory` + `CrudDao` + custom DAOs) |
| ORM | Hibernate 6 (Jakarta Persistence) |
| Database | Supabase — hosted PostgreSQL (JDBC) |
| Build | Maven, Java 21+ |

## Architecture

The app follows a classic layered desktop architecture:

```
FXML views (src/main/resources/view)
        │  fx:id / onAction bindings
Controllers (edu.icet.demo.controller)
        │  models (edu.icet.demo.model)
BO layer (edu.icet.demo.bo — BoFactory, custom BOs)
        │  entities (edu.icet.demo.entity)
DAO layer (edu.icet.demo.dao — DaoFactory, CrudDao, custom DAOs)
        │
Hibernate (edu.icet.demo.util.HibernateUtil) ──► Supabase PostgreSQL
```

## Screenshots

| Screen | Description |
|---|---|
| Welcome & Login | Split-screen layout: dark brand panel with the product mark, clean sign-in card |
| Admin Dashboard | Dark navigation sidebar + employee account management |
| Products / Suppliers | Business-style data tables with side form cards for add / update / delete |
| Place Order | Customer picker, product cart with live total and stock checks |
| Order History | All orders with total revenue summary |

> Run the app and take your own screenshots — place them in `src/main/resources/images/` to embed them here.

## Setting up Supabase (one-time, ~5 minutes)

1. **Create a project** — sign up at [supabase.com](https://supabase.com), click **New project**, choose a name, region and a **database password** (save it!).
2. **Create the tables** — in the dashboard open **SQL Editor → New query**, paste the full contents of [`database/schema.sql`](database/schema.sql) and click **Run**. This creates all tables and seeds the default owner account.
3. **Get the connection string** — open **Project Settings → Database → Connection string** (or click **Connect** at the top of the dashboard) and note the host, port and user. Two options:
   - **Session pooler (recommended — works on regular IPv4 networks):**
     host `aws-<n>-<region>.pooler.supabase.com` (copy it exactly as shown — the `aws-0`/`aws-1` prefix and region vary per project), port `5432`, user `postgres.<project-ref>`
   - **Direct connection (requires IPv6):**
     host `db.<project-ref>.supabase.co`, port `5432`, user `postgres`
4. **Configure the app** — open `src/main/resources/application.properties` and fill in (note: this is the *database* connection string, **not** the project's `https://<ref>.supabase.co` API URL):

   ```properties
   db.url=jdbc:postgresql://<session-pooler-host>:5432/postgres?sslmode=require
   db.username=postgres.<project-ref>
   db.password=<your database password>
   ```

That's it. The app keeps `hibernate.hbm2ddl.auto=update` enabled as a safety net, so it can also create missing tables by itself on first login.

## Building and running

Requirements: **JDK 21 or newer** and **Maven 3.8+**.

```bash
# compile
mvn clean compile

# run the app
mvn javafx:run
```

## Default login

| Username | Password |
|---|---|
| `admin` | `admin123` |

> **Important:** change this password after your first login (or update the `users` row in Supabase with a new SHA-256 hash). The default account is also created automatically on first login if the `users` table is empty.

Passwords are stored as SHA-256 hashes — never in plain text.

## Troubleshooting

| Problem | Fix |
|---|---|
| *"Could not connect to the database"* dialog | Check `application.properties` — URL, username and password must match your Supabase project. Make sure the project is not paused (free projects pause after inactivity; open the dashboard to wake it up). |
| Connection works at home but not on office Wi-Fi | Use the **session pooler** connection (option A) — the direct host is IPv6-only and some networks block it. |
| `Invalid username or password` on first run | Run `database/schema.sql` in the Supabase SQL Editor, or simply try again — the app seeds the default `admin` account automatically when the users table is empty. |
| Cannot delete a supplier/product | Records referenced by existing products/orders are protected by foreign keys. Remove the dependent records first. |
| App window opens but is blank/odd | Make sure you launched it with `mvn javafx:run` (this sets up the JavaFX runtime correctly). |

## License

Proprietary — all rights reserved.
