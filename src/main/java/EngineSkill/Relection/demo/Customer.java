package EngineSkill.Relection.demo;

public class Customer {
    private Long id;
    private String name;
    private int age;

    public Customer(Long id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public Customer() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }



    public static void main(String[] args) throws Exception {
        Customer customer = new Customer();
        customer.setAge(20);
        customer.setId(2L);
        customer.setName("88");

        new RelectTest().copy(customer);

    }






}
