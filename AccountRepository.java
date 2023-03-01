package com.codility;

public class AccountRepository {
    MySqlConnection connection;

    public AccountRepository() {
    }

    public AccountRepository(MySqlConnection connection) {
        connection = connection;
    }

    void transferMoney(Account a, Account b, int amount) {
        b.addBalance(amount);
        a.reduceBalance(amount);
        this.update(a);
        this.update(b);
    }

    void update(Account account) {
        Transaction transaction = connection.beginTransaction(); // begin database transaction
        try {
            connection.save(account);
            connection.commit();
            connection.closeTransaction(transaction);
        } catch (Exception e) {
            log.error("Error on update account id: ["+account.getId()+"]. Error: "+e.getMessage());
        }
    }

    /**
     * To retrieve top ten accounts sorted by balance amount (desc),
     * that can be filtered by some specific fields.
     * Example: retrieve top ten highest balance accounts at branch Putrajaya
     *
     * @param query - query of the account
     * @return top accounts
     */
    public List<Account> retrieve_top_accounts(String query) {
        List<Account> accounts = connection.query(query);
        accounts.sort(Comparator.comparing(Account::getBalance).reversed());
        return accounts.stream().limit(10).collect(Collectors.<Account>toList());
    }
}
