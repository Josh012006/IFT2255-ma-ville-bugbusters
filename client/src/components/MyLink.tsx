import type { ReactNode } from "react";
import { Link, type LinkProps } from "react-router-dom";

type MyLinkProps = {
  to: string;
  className?: string;
  children: ReactNode;
} & Omit<LinkProps, "to" | "className" | "children">;


/**
 * Une classe wrapper qui offre un style préfait pour les liens HTML.
 * Elle représente un lien dans le routage.
 * @param to qui représente l'url du lien
 * @param className qui est l'attribut class à mettre sur le composant
 * @param children qui est l'enfant du composant, donc l'élement affiché par le lien
 * @param rest le reste des attributs éventuels à passer au lien
 * @return ReactNode.
 */
export default function MyLink({ to, className = "", children, ...rest }: MyLinkProps) {
  return (
    <Link className={`text-decoration-none  ${className}`} to={to} {...rest}>
      {children}
    </Link>
  );
}
