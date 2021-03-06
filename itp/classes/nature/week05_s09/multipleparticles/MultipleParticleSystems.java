import processing.core.*; 
import processing.xml.*; 

import java.applet.*; 
import java.awt.*; 
import java.awt.image.*; 
import java.awt.event.*; 
import java.io.*; 
import java.net.*; 
import java.text.*; 
import java.util.*; 
import java.util.zip.*; 
import java.util.regex.*; 

public class MultipleParticleSystems extends PApplet {


// Simple Particle System
// Daniel Shiffman <http://www.shiffman.net>

// Click the mouse to generate a burst of particles
// at mouse location

// Each burst is one instance of a particle system
// with Particles and CrazyParticles (a subclass of Particle)
// Note use of Inheritance and Polymorphism here

ArrayList psystems;

public void setup() {
  size(200,200);
  psystems = new ArrayList();
  smooth();
}

public void draw() {
  background(255);

  // Cycle through all particle systems, run them and delete old ones
  for (int i = psystems.size()-1; i >= 0; i--) {
    ParticleSystem psys = (ParticleSystem) psystems.get(i);
    psys.run();
    if (psys.dead()) {
      psystems.remove(i);
    }
  }
  
}

// When the mouse is pressed, add a new particle system
public void mousePressed() {
  psystems.add(new ParticleSystem(PApplet.parseInt(random(5,25)),new PVector(mouseX,mouseY)));
}


// Simple Particle System
// Daniel Shiffman <http://www.shiffman.net>

// A subclass of Particle

class CrazyParticle extends Particle {

  // Just adding one new variable to a CrazyParticle
  // It inherits all other fields from "Particle", and we don't have to retype them!
  float theta;

  // The CrazyParticle constructor can call the parent class (super class) constructor
  CrazyParticle(PVector l) {
    // "super" means do everything from the constructor in Particle
    super(l);
    // One more line of code to deal with the new variable, theta
    theta = 0.0f;

  }

  // Notice we don't have the method run() here; it is inherited from Particle

  // This update() method overrides the parent class update() method
  public void update() {
    super.update();
    // Increment rotation based on horizontal dir & mag of vel
    float theta_vel = (vel.x * vel.mag()) / 10.0f;
    theta += theta_vel;
  }

  // Override timer
  public void timer() {
    timer -= 0.5f;
  }
  
  // Method to display
  public void render() {
    // Render the ellipse just like in a regular particle
    super.render();

    // Then add a rotating line
    pushMatrix();
    translate(loc.x,loc.y);
    rotate(theta);
    stroke(0,timer);
    line(0,0,25,0);
    popMatrix();
  }
}

// Simple Particle System
// Daniel Shiffman <http://www.shiffman.net>

// A simple Particle class

class Particle {
  PVector loc;
  PVector vel;
  PVector acc;
  float r;
  float timer;

  // One constructor
  Particle(PVector a, PVector v, PVector l, float r_) {
    acc = a.get();
    vel = v.get();
    loc = l.get();
    r = r_;
    timer = 150.0f;
  }

  // Another constructor (the one we are using here)
  Particle(PVector l) {
    acc = new PVector(0,0.05f,0);
    vel = new PVector(random(-1,1),random(-2,0),0);
    loc = l.get();
    r = 10.0f;
    timer = 100.0f;
  }


  public void run() {
    update();
    timer();
    render();
  }

  // Method to update location
  public void update() {
    vel.add(acc);
    loc.add(vel);
  }

  public void timer() {
    timer -= 1.0f;
  }

  // Method to display
  public void render() {
    ellipseMode(CENTER);
    stroke(0,timer);
    fill(0,timer);
    ellipse(loc.x,loc.y,r,r);
  }

  // Is the particle still useful?
  public boolean dead() {
    if (timer <= 0.0f) {
      return true;
    } 
    else {
      return false;
    }
  }
}


// Simple Particle System
// Daniel Shiffman <http://www.shiffman.net>

// A class to describe a group of Particles
// An ArrayList is used to manage the list of Particles

class ParticleSystem {

  ArrayList particles;    // An arraylist for all the particles
  PVector origin;        // An origin point for where particles are birthed

  ParticleSystem(int num, PVector v) {
    particles = new ArrayList();              // Initialize the arraylist
    origin = v.get();                        // Store the origin point
    // Add an initial group of particles to the ArrayList
    for (int i = 0; i < num; i++) {
      if (random(1) < 0.6f) {
        particles.add(new Particle(origin));
      } else {
        // There is a 40% chance we will add a "crazy particle" into the system
        particles.add(new CrazyParticle(origin));
      }
    }
  }

  public void run() {
    // Cycle through the ArrayList backwards b/c we are deleting
    for (int i = particles.size()-1; i >= 0; i--) {
      Particle p = (Particle) particles.get(i);
      p.run();
      if (p.dead()) {
        particles.remove(i);
      }
    }
  }

  public void addParticle() {
    particles.add(new Particle(origin));
  }

  public void addParticle(Particle p) {
    particles.add(p);
  }

  // A method to test if the particle system still has particles
  public boolean dead() {
    if (particles.isEmpty()) {
      return true;
    } else {
      return false;
    }
  }

}


  static public void main(String args[]) {
    PApplet.main(new String[] { "--bgcolor=#c0c0c0", "MultipleParticleSystems" });
  }
}
