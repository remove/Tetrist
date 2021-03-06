package Graphic;

import java.util.Vector;
import java.awt.Graphics;
import javax.swing.JPanel;
import java.awt.Dimension;

import Component.Game;
import Graphic.Abstract.DrawPart;
import Graphic.Abstract.DrawGrid;
import Graphic.Abstract.DrawNext;
import Graphic.Abstract.DrawBlock;
import Graphic.Abstract.DrawInfos;
import Graphic.Abstract.DrawBackground;

import Graphic.DrawBasic;
import Graphic.DrawNice;

/**
 * Classe de l'affichage actuel.
 */
public abstract class Draw extends JPanel
{
    ///Tous les attributs présents dans l'affichage d'une partie de Tetrist.
    protected final Game game;
    protected final DrawGrid draw_grid;
    protected final DrawNext draw_next;
    protected final DrawBlock draw_block;
    protected final DrawInfos draw_infos;
    protected final DrawBackground draw_bg;
    protected final DrawPart[] parts;
    protected int window_width;
    protected int window_height;

    /// Constructeur d'une partie de Tetrist.
    public Draw(Game g, DrawGrid dg, DrawNext dn, DrawBlock db, DrawInfos di)
    {
        super();
        game = g;

        draw_grid = dg;
        draw_next = dn;
        draw_block = db;
        draw_infos = di;
        parts = new DrawPart[] { dg, dn, di };

        compute_size();
        set_size();
        draw_bg = create_draw_background(window_width, window_height);
    }

    /**
     * Dessin d'un jeu de Tetrist.
     *
     * @param g
     *          Variable employée pour le dessin dans une fenêtre.
     */
    public void paint(Graphics g)
    {
        super.paint(g);

        draw_bg.paint(g);
        draw_grid.paint(g);
        draw_next.paint(g);
        draw_infos.paint(g);
    }

    public abstract DrawBackground create_draw_background(int w, int h);

    /**
     * Rafraîchissement de l'affichage.
     */
    public void refresh()
    {
        game.refresh();
        repaint(getVisibleRect());
    }

    private void compute_size()
    {
        int width = 0;
        int height = 0;

        for (DrawPart part : parts)
        {
            int part_x = part.offset_x();
            int part_y = part.offset_y();
            int part_width = part.width();
            int part_height = part.height();

            if (part_x < width)
            {
                if (part_x + part_width > width)
                    width += part_width - (width - part_x);
            }
            else
                width += part_width + (part_x - width);

            if (part_y < height)
            {
                if (part_y + part_height > height)
                    height += part_height - (height - part_y);
            }
            else
                height += part_height + (part_y - height);
        }

        window_width = width;
        window_height = height;
    }

    /**
     * Modifie la taille de la fenêtre.
     */
    protected void set_size()
    {
        setPreferredSize(new Dimension(window_width, window_height));
    }

    /**
     * Dessine selon les possibilités.</br>
     * Soit <b>avec image et font</b> soit <b>sans</b>.
     *
     * @param g
     *          Variable du jeu Tetrist.
     */
    public static Draw factory(Game g)
    {
        Draw draw = null;
        try
        {
            draw = new DrawNice(g);
        }
        catch (Exception e)
        {
            draw = new DrawBasic(g);
        }

        return draw;
    }
}
